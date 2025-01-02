// src/main/java/com/example/backend/domain/preventive_maintenance/entity/MaintenanceSchedule.java

package com.example.backend.domain.preventive_maintenance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id", nullable = false)
    private Long siteId;

    @Column(name = "pack_id", nullable = false)
    private Long packId;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "year", nullable = false)
    private Integer year;
}
