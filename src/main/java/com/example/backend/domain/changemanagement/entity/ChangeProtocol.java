package com.example.backend.domain.changemanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.example.backend.domain.preventive_maintenance.entity.Project;  // Import the Project entity

@Entity
@Table(name = "change_protocols")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeProtocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chaine;
    private String panneauModule;
    private String numeroPanneauStart;
    private String numeroPanneauEnd;

    private String modification;
    private String brettbau;
    private LocalDateTime dateBrettbau;
    private String pqmValidation;
    private LocalDateTime datePqm;
    private String pprValidation;
    private LocalDateTime datePpr;
    private String remark;
    private String remarque; 

    // New relationship to Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    // Additional fields for metadata if needed
}
