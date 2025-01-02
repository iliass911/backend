package com.example.backend.domain.preventive_maintenance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private Integer year;
    private Long siteId; // Reference to Site
}
