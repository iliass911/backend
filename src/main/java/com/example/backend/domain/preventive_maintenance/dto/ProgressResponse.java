// src/main/java/com/example/backend/domain/preventive_maintenance/dto/ProgressResponse.java
package com.example.backend.domain.preventive_maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponse {
    private long total;
    private long completed;
    private long advanced;
    private long retard;
    private String status; // "Advanced" or "Retard"
}