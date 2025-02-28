package com.sebn.brettbau.domain.implplan.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "implementation_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImplementationPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String side;
    private String fbType;
    private String phase;
    private String comment;

    @Column(name = "fb_title")
    private String fbTitle;

    @Column(name = "statut_release_pqm")
    private String statutReleasePqm;

    @Column(name = "cad_designer_name")
    private String cadDesignerName;

    @Column(name = "visualisation_status")
    private String visualisationStatus;

    @Column(name = "visualisation_sent_to_pqm_date")
    private LocalDate visualisationSentToPqmDate;

    @Column(name = "pqm_release_visualisation")
    private String pqmReleaseVisualisation;

    @Column(name = "implementation_on_fb")
    private String implementationOnFb;

    // Implementation Start/End
    @Column(name = "implementation_start_date")
    private LocalDate implementationStartDate;

    @Column(name = "implementation_end_date")
    private LocalDate implementationEndDate;

    @Column(name = "implementation_statut")
    private String implementationStatut;

    @Column(name = "delivered_to_pqm_date")
    private LocalDate deliveredToPqmDate;

    // Release Start/End
    @Column(name = "release_start_date")
    private LocalDate releaseStartDate;

    @Column(name = "release_end_date")
    private LocalDate releaseEndDate;

    @Column(name = "first_check_pqm")
    private String firstCheckPqm;

    @Column(name = "reparation_bb")
    private String reparationBb;

    @Column(name = "second_check_pqm")
    private String secondCheckPqm;

    @Column(name = "yellow_release_date")
    private LocalDate yellowReleaseDate;

    @Column(name = "wh_sample_status")
    private String whSampleStatus;

    @Column(name = "orange_release_date")
    private LocalDate orangeReleaseDate;

    @Column(name = "green_release_date")
    private LocalDate greenReleaseDate;

    @Column(name = "cablage_simulation_tc")
    private String cablageSimulationTc;

    // Add any other fields if needed
}
