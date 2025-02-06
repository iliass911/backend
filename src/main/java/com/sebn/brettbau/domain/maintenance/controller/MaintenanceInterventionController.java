package com.sebn.brettbau.domain.maintenance.controller;

import com.sebn.brettbau.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.sebn.brettbau.domain.maintenance.service.MaintenanceInterventionService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "http://10.150.2.201:3000")
@Validated
public class MaintenanceInterventionController {
    private final MaintenanceInterventionService service;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public MaintenanceInterventionController(
            MaintenanceInterventionService service,
            RoleService roleService,
            UserService userService) {
        this.service = service;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceInterventionDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read maintenance interventions");
        }
        return ResponseEntity.ok(service.getAllInterventions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceInterventionDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read maintenance interventions");
        }
        return ResponseEntity.ok(service.getInterventionById(id));
    }

    @PostMapping
    public ResponseEntity<MaintenanceInterventionDTO> create(
            @RequestBody @Validated MaintenanceInterventionDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create maintenance interventions");
        }
        return ResponseEntity.ok(service.createIntervention(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceInterventionDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated MaintenanceInterventionDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update maintenance interventions");
        }
        return ResponseEntity.ok(service.updateIntervention(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete maintenance interventions");
        }
        service.deleteIntervention(id);
        return ResponseEntity.noContent().build();
    }
}
