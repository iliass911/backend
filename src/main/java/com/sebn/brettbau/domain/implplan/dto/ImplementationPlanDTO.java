package com.sebn.brettbau.domain.implplan.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ImplementationPlanDTO {

    private Long id;
    private String side;
    private String fbType;
    private String phase;
    private String comment;
    private String fbTitle;
    private String statutReleasePqm;
    private String cadDesignerName;
    private String visualisationStatus;
    private LocalDate visualisationSentToPqmDate;
    private String pqmReleaseVisualisation;
    private String implementationOnFb;
    private LocalDate implementationStartDate;
    private LocalDate implementationEndDate;
    private String implementationStatut;
    private LocalDate deliveredToPqmDate;
    private LocalDate releaseStartDate;
    private LocalDate releaseEndDate;
    private String firstCheckPqm;
    private String reparationBb;
    private String secondCheckPqm;
    private LocalDate yellowReleaseDate;
    private String whSampleStatus;
    private LocalDate orangeReleaseDate;
    private LocalDate greenReleaseDate;
    private String cablageSimulationTc;
}
