package udemy.webscrapping.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaGoogleDTO implements Serializable {
    private static final long seralVersionUID = 1L;
    private String statusPartida;
    private String tempoPartida;
    //Info equipe da casa
    private String urlLogoEquipeCasa;
    private String placarEquipeCasa;
    private String golsEquipeCasa;
    private String placarEstendidoEquipeCasa;

    //Info equipe visitante
    private String urlLogoEquipeVisitante;
    private String placarEquipeVisitante;
    private String golsEquipeVisitante;
    private String placarEstendidoEquipeVisitante;


}
