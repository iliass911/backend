package com.sebn.brettbau.domain.preventive_maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardFamilyDTO {
    private Long id;

    @NotBlank(message = "Family name is required")
    private String familyName;

    @NotBlank(message = "Projet is required")
    private String projet;

    @NotBlank(message = "Side is required")
    private String side;

    @NotBlank(message = "FB Type 2 is required")
    private String fbType2;

    @NotBlank(message = "FB Type 3 is required")
    private String fbType3;

    @NotBlank(message = "FB Size is required")
    private String fbSize;

    @NotBlank(message = "Derivate is required")
    private String derivate;

    private Integer boardCount;

    private String createdAt;
}
