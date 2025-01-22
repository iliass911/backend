package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ProjectDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.ProjectService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final RoleService roleService;

    public ProjectController(ProjectService projectService, UserService userService, RoleService roleService) {
        this.projectService = projectService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.PROJECT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ projects.");
        }
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.PROJECT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ projects.");
        }
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.PROJECT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE projects.");
        }
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.PROJECT, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE projects.");
        }
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.PROJECT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE projects.");
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
