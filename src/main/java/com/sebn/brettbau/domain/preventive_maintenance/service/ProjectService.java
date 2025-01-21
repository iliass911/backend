package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ProjectDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Project;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.ProjectMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.ProjectRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SiteRepository siteRepository;

    public ProjectService(ProjectRepository projectRepository, SiteRepository siteRepository) {
        this.projectRepository = projectRepository;
        this.siteRepository = siteRepository;
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
        return ProjectMapper.toDTO(project);
    }

    public ProjectDTO createProject(ProjectDTO dto) {
        Project project = ProjectMapper.toEntity(dto);
        // Set associated Site
        project.setSite(siteRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new RuntimeException("Site not found with id " + dto.getSiteId())));
        Project saved = projectRepository.save(project);
        return ProjectMapper.toDTO(saved);
    }

    public ProjectDTO updateProject(Long id, ProjectDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
        project.setName(dto.getName());
        project.setYear(dto.getYear());
        if (dto.getSiteId() != null) {
            project.setSite(siteRepository.findById(dto.getSiteId())
                    .orElseThrow(() -> new RuntimeException("Site not found with id " + dto.getSiteId())));
        }
        Project updated = projectRepository.save(project);
        return ProjectMapper.toDTO(updated);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id " + id);
        }
        projectRepository.deleteById(id);
    }
}
