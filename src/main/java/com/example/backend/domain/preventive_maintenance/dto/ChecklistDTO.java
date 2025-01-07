package com.example.backend.domain.preventive_maintenance.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDTO {
    private Long id;
    private Long boardId;
    private String technicianName;
    private String qualityAgentName;
    private Integer completionPercentage;
    private String comments;
    private Boolean qualityValidated;
    private LocalDateTime validationDate;
    private LocalDateTime expiryDate;
    private String workStatus;
    private LocalDateTime createdAt;
    
    // New field for week number
    private Integer weekNumber;
}
