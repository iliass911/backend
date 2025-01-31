package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.PhaseDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.PhaseService;
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
@RequestMapping("/api/phases")
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
public class PhaseController {

    private final PhaseService phaseService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public PhaseController(
            PhaseService phaseService,
            RoleService roleService,
            UserService userService
    ) {
        this.phaseService = phaseService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PhaseDTO>> getAllPhases(
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.PHASE);
            List<PhaseDTO> phases = phaseService.getAllPhases();
            return ResponseEntity.ok(phases);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhaseDTO> getPhaseById(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.PHASE);
            PhaseDTO dto = phaseService.getPhaseById(id);
            return ResponseEntity.ok(dto);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<PhaseDTO> createPhase(
        @RequestBody PhaseDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.PHASE);
            PhaseDTO created = phaseService.createPhase(dto);
            return ResponseEntity.ok(created);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhaseDTO> updatePhase(
        @PathVariable Long id,
        @RequestBody PhaseDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.UPDATE, requestingModule, Module.PHASE);
            PhaseDTO updated = phaseService.updatePhase(id, dto);
            return ResponseEntity.ok(updated);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.PHASE);
            phaseService.deletePhase(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private void verifyPermission(PermissionType permissionType, String requestingModule, Module targetModule) {
        User currentUser = userService.getCurrentUser();
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

