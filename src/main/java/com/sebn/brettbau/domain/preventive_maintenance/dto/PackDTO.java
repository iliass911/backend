// src/main/java/com/example/backend/domain/preventive_maintenance/dto/PackDTO.java
package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PackDTO {
    private Long id;

    @NotBlank(message = "Pack name is mandatory")
    private String name;

    @NotNull(message = "Site ID is mandatory")
    private Long siteId;

    @NotNull(message = "Project ID is mandatory")
    private Long projectId;
}
