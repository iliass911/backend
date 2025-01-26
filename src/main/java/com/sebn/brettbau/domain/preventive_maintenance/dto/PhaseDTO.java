package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.Data;

@Data
public class PhaseDTO {
    private Long id;
    private String name;
    private Long projectId;
}
