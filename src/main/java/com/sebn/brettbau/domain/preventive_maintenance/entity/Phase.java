package com.sebn.brettbau.domain.preventive_maintenance.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // If you want a direct relationship to a Project entity, 
    // replace with @ManyToOne, etc.
    @Column
    private Long projectId;
}
