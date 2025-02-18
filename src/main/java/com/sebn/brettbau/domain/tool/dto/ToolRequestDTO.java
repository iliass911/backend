package com.sebn.brettbau.domain.tool.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ToolRequestDTO {
    private Long id;
    private Long toolId;
    private String requestedBy;
    private String approvedBy;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime expectedReturnDate;
    private String purpose;
    private String notes;
}
