package com.sebn.brettbau.domain.tool.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ToolDTO {
    private Long id;
    private String toolId;
    private String name;
    private String brand;
    private String type;
    private String location;
    private String condition;
    private String status;
    private String currentHolder;
    private LocalDateTime lastMaintained;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
