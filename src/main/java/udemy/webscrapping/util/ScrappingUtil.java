package udemy.webscrapping.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import udemy.webscrapping.dto.PartidaGoogleDTO;

import java.io.IOException;

public class ScrappingUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(ScrappingUtil.class);
    private static final String BASE_SEARCH_URL_GOOGLE = "https://www.google.com/search?q=";
    private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";
    public static void main(String[] args) {
        String url = BASE_SEARCH_URL_GOOGLE + "corinthians+x+fluminense" + COMPLEMENTO_URL_GOOGLE;
        ScrappingUtil scrappingUtil = new ScrappingUtil();
        scrappingUtil.obterInformacoesPartida(url);

    }

    public PartidaGoogleDTO obterInformacoesPartida(String url){
        PartidaGoogleDTO partida = new PartidaGoogleDTO();
        try {
            Document document = Jsoup.connect(url).get();
            String title = document.title();
            LOGGER.info("Titulo da pagina: " + title);
        } catch (IOException e) {
            LOGGER.error("Erro ao tentar conectar no google com JSOUP -> " + e.getMessage());
        }
        return  partida;
    }
}
