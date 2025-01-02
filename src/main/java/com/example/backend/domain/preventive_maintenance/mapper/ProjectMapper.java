package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.ProjectDTO;
import com.example.backend.domain.preventive_maintenance.entity.Project;

public class ProjectMapper {
    public static ProjectDTO toDTO(Project project) {
        if (project == null) return null;
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .year(project.getYear())
                .siteId(project.getSite() != null ? project.getSite().getId() : null)
                .build();
    }

    public static Project toEntity(ProjectDTO dto) {
        if (dto == null) return null;
        Project project = Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .year(dto.getYear())
                // Assuming you have a method to fetch Site by ID, e.g., from a service
                // .site(siteService.findById(dto.getSiteId()))
                .build();
        return project;
    }
}
