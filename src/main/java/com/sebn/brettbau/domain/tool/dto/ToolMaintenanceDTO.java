package com.sebn.brettbau.domain.tool.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ToolMaintenanceDTO {
    private Long id;
    private Long toolId;
    private String type;
    private String requestedBy;
    private String description;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private String notes;
    private Double cost;
}
	