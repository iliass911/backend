// src/main/java/com/example/backend/domain/preventive_maintenance/dto/ProgressResponse.java
package com.example.backend.domain.preventive_maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProgressResponse {
    private Long totalChecklists;
    private Long completedChecklists;
    private Long pendingChecklists;
    private String status; // e.g., "Advanced", "Retard", "Completed"
}
