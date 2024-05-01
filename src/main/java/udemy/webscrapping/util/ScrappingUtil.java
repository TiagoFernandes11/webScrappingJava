package udemy.webscrapping.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import udemy.webscrapping.dto.PartidaGoogleDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrappingUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrappingUtil.class);
    private static final String BASE_SEARCH_URL_GOOGLE = "https://www.google.com/search?q=";
    private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";

    public static void main(String[] args) {
        String url = BASE_SEARCH_URL_GOOGLE + "fortaleza+x+brangantino" + COMPLEMENTO_URL_GOOGLE;
        ScrappingUtil scrappingUtil = new ScrappingUtil();
        scrappingUtil.obterInformacoesPartida(url);
    }

    public PartidaGoogleDTO obterInformacoesPartida(String url) {
        PartidaGoogleDTO partida = new PartidaGoogleDTO();
        try {
            Document document = Jsoup.connect(url).get();
            StatusPartida statusDaPartida = obterStatusDaPartida(document);
            LOGGER.info("Partida: " + document.title());
            LOGGER.info("status da partida: " + statusDaPartida);
            if (statusDaPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
                LOGGER.info("Placar da casa: " + obterResultadoDaPartida(document));
                LOGGER.info("Tempo da partida: " + obterTempoPartida(document));
                LOGGER.info("Gols time da casa: " + obterGolsPartidaCasa(document));
                LOGGER.info("Gols time visitante: " + obterGolsPartidaVisistante(document));
            }
            LOGGER.info("Nome da equipe da casa: " + obterNomeDaEquipeDaCasa(document));
            LOGGER.info("Nome da equipe visitante: " + obterNomeDaEquipeVisitante(document));
            LOGGER.info("URL do logo do time da casa: " + obterLogoEquipeDaCasa(document));
            LOGGER.info("URL do logo do time vistante: " + obterLogoEquipeVisitante(document));

        } catch (IOException e) {
            LOGGER.error("Erro ao tentar conectar no google com JSOUP -> " + e.getMessage());
        }
        return partida;
    }

    private StatusPartida obterStatusDaPartida(Document document) {
        StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;
        boolean isNotTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
        if (!isNotTempoPartida) {
            String tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
            statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
            if (tempoPartida.contains("Pênaltis")) {
                statusPartida = StatusPartida.PARTIDA_PENALTIS;
            }
        }
        isNotTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
        if (!isNotTempoPartida) {
            statusPartida = StatusPartida.PARTIDA_ENCERRADA;
        }
        return statusPartida;
    }

    private String obterTempoPartida(Document document) {
        String tempoPartida = null;
        boolean isNotTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
        if (!isNotTempoPartida) {
            tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
        }
        isNotTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
        if (!isNotTempoPartida) {
            tempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first().text();
        }
        return tempoPartida;
    }

    private String obterResultadoDaPartida(Document document) {
        Integer placarVisitante = obterPlacarEquipeVisitante(document);
        Integer placarDaCasa = obterPlacarEquipeCasa(document);
        return "Placar: " + placarDaCasa + " x " + placarVisitante;
    }

    private Integer obterPlacarEquipeCasa(Document document){
        return Integer.parseInt(document.select("div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]").first().text());
    }

    private Integer obterPlacarEquipeVisitante(Document document){
        return Integer.parseInt(document.select("div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]").first().text());
    }

    private String obterNomeDaEquipeDaCasa(Document document) {
        Element element = document.select("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]").first();
        return element.select("span").text();
    }

    private String obterNomeDaEquipeVisitante(Document document) {
        Element element = document.select("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]").first();
        return element.select("span").text();
    }

    private String obterLogoEquipeDaCasa(Document document) {
        Element element = document.select("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]").first();
        return element.select("img[class=imso_btl__mh-logo]").attr("src");
    }

    private String obterLogoEquipeVisitante(Document document) {
        Element element = document.select("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]").first();
        return element.select("img[class=imso_btl__mh-logo]").attr("src");
    }

    private String obterGolsPartidaCasa(Document document){
        List<String> golsEquipe = new ArrayList<>();
        Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__left-team]").select("div[class=imso_gs__gs-r]");
        for(Element e : elementos){
            golsEquipe.add(e.select("div[class=imso_gs__gs-r]").text());
        }
        return String.join(", ", golsEquipe);
    }

    private String obterGolsPartidaVisistante(Document document){
        List<String> golsEquipe = new ArrayList<>();
        Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__right-team]").select("div[class=imso_gs__gs-r]");
        for(Element e : elementos){
            golsEquipe.add(e.select("div[class=imso_gs__gs-r]").text());
        }
        return String.join(", ", golsEquipe);
    }

}
