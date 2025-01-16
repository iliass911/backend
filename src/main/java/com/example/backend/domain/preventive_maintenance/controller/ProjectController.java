// ProjectController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.ProjectDTO;
import com.example.backend.domain.preventive_maintenance.service.ProjectService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    public ProjectController(PermissionChecker permissionChecker, ProjectService projectService) {
        super(permissionChecker);
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        checkPermission("PROJECT", "VIEW");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        checkPermission("PROJECT", "VIEW");
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO dto) {
        checkPermission("PROJECT", "CREATE");
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        checkPermission("PROJECT", "UPDATE");
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        checkPermission("PROJECT", "DELETE");
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}