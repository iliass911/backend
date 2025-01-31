package com.sebn.brettbau.domain.action.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "action_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

