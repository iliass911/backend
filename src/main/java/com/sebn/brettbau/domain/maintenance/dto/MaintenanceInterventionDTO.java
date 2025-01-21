package com.sebn.brettbau.domain.maintenance.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MaintenanceInterventionDTO {
    private Long id;

    @NotBlank
    private String typeIntervention;

    @NotNull
    private Double valeurEuro;

    private String pointExaminer;
    private String natureIntervention;
    private String posteTouche;
    private String zone;
    private String shift;
    private String numeroPanneau;
    private String nomPrenomBB;

    @NotNull
    private Double tempsIntervention;

    @NotNull
    private LocalDate date;

    private String commentaire;
    private String site;
}
