// src/main/java/com/example/backend/domain/preventive_maintenance/dto/MaintenanceScheduleDTO.java
package com.example.backend.domain.preventive_maintenance.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaintenanceScheduleDTO {
    private Long id;

    @NotNull(message = "Project ID is mandatory")
    private Long projectId;

    @NotNull(message = "Pack ID is mandatory")
    private Long packId;

    @NotNull(message = "Week Number is mandatory")
    private Integer weekNumber;

    @NotNull(message = "Year is mandatory")
    private Integer year;
}

