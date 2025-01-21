package com.sebn.brettbau.domain.action.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ActionRecordDTO {
    private Long id;
    private String action;
    private String priority;
    private String responsable;
    private String projet;
    private String phaseProjet;
    private String typeIntervention;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String kw;
    private String mois;
    private String matricule;
    private String statut;
    private String remarque;
}
