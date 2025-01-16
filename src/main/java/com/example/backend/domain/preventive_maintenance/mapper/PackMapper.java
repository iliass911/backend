// PackMapper.java
package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.PackDTO;
import com.example.backend.domain.preventive_maintenance.entity.Pack;
import com.example.backend.domain.preventive_maintenance.entity.Site;
import com.example.backend.domain.preventive_maintenance.entity.Project;
import com.example.backend.domain.preventive_maintenance.repository.SiteRepository;
import com.example.backend.domain.preventive_maintenance.repository.ProjectRepository;
import com.example.backend.exception.ResourceNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PackMapper {
    @Autowired
    protected SiteRepository siteRepository;
    
    @Autowired
    protected ProjectRepository projectRepository;

    @Mapping(source = "site.id", target = "siteId")
    @Mapping(source = "project.id", target = "projectId")
    public abstract PackDTO toDTO(Pack pack);

    @Mapping(target = "site", expression = "java(getSite(packDTO.getSiteId()))")
    @Mapping(target = "project", expression = "java(getProject(packDTO.getProjectId()))")
    public abstract Pack toEntity(PackDTO packDTO);

    protected Site getSite(Long siteId) {
        return siteRepository.findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException("Site not found with id " + siteId));
    }

    protected Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
    }
}