package com.sebn.brettbau.domain.tool.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ToolTransferDTO {
    private Long id;
    private Long toolId;
    private String fromUser;
    private String toUser;
    private String conditionBefore;
    private String conditionAfter;
    private LocalDateTime transferredAt;
    private String notes;
    private String photoUrls;
    private String status;
}
