package com.sebn.brettbau.domain.maintenance.controller;

import com.sebn.brettbau.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.sebn.brettbau.domain.maintenance.service.MaintenanceInterventionService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@Validated
public class MaintenanceInterventionController {

    private final MaintenanceInterventionService service;
    private final UserService userService;
    private final RoleService roleService;

    public MaintenanceInterventionController(MaintenanceInterventionService service,
                                             UserService userService,
                                             RoleService roleService) {
        this.service = service;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<MaintenanceInterventionDTO> create(
            @RequestBody @Validated MaintenanceInterventionDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE maintenance interventions.");
        }
        MaintenanceInterventionDTO result = service.createIntervention(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceInterventionDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ maintenance interventions.");
        }
        List<MaintenanceInterventionDTO> result = service.getAllInterventions();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceInterventionDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ maintenance interventions.");
        }
        MaintenanceInterventionDTO result = service.getInterventionById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceInterventionDTO> update(
            @PathVariable Long id, 
            @RequestBody @Validated MaintenanceInterventionDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE maintenance interventions.");
        }
        MaintenanceInterventionDTO result = service.updateIntervention(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE maintenance interventions.");
        }
        service.deleteIntervention(id);
        return ResponseEntity.noContent().build();
    }
}
