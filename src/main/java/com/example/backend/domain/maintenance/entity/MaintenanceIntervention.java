package com.example.backend.domain.maintenance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_interventions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceIntervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeIntervention;
    private Double valeurEuro;
    private String pointExaminer;
    private String natureIntervention;
    private String posteTouche;
    private String zone;
    private String shift;
    private String numeroPanneau;
    private String nomPrenomBB;
    private Double tempsIntervention;
    private LocalDate date;
    private String commentaire;
    private String site;
}
