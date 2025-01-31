package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ProjectDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.ProjectService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(
    origins = "http://localhost:3000",
    allowedHeaders = {
        "Origin", 
        "Content-Type", 
        "Accept", 
        "Authorization",
        "X-Requesting-Module"
    }
)
public class ProjectController {

    private final ProjectService projectService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, RoleService roleService, UserService userService) {
        this.projectService = projectService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.PROJECT);
            List<ProjectDTO> projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.PROJECT);
            ProjectDTO project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
        @RequestBody ProjectDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.PROJECT);
            ProjectDTO createdProject = projectService.createProject(dto);
            return ResponseEntity.ok(createdProject);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(
        @PathVariable Long id,
        @RequestBody ProjectDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.UPDATE, requestingModule, Module.PROJECT);
            ProjectDTO updatedProject = projectService.updateProject(id, dto);
            return ResponseEntity.ok(updatedProject);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.PROJECT);
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private void verifyPermission(PermissionType permissionType, String requestingModule, Module targetModule) {
        User currentUser = userService.getCurrentUser();

        // parse requestingModule -> callingModule
        Module callingModule = null;
        if (requestingModule != null) {
            try {
                callingModule = Module.valueOf(requestingModule.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
            }
        }

        boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                currentUser.getRole(),
                targetModule,
                permissionType,
                callingModule
        );

        if (!hasAccess) {
            throw new AccessDeniedException("No permission to " + permissionType + " in " + targetModule);
        }
    }
}

