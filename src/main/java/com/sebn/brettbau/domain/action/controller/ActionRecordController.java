package com.sebn.brettbau.domain.action.controller;

import com.sebn.brettbau.domain.action.dto.ActionRecordDTO;
import com.sebn.brettbau.domain.action.service.ActionRecordService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionRecordController {
    private final ActionRecordService service;

    // Dummy permission check method for demonstration.
    private boolean hasPermission(Authentication auth, Module module, PermissionType permission) {
        // Simplified: allow actions only for users with ROLE_ADMIN.
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public ResponseEntity<List<ActionRecordDTO>> getAll(Authentication auth) {
        if (!hasPermission(auth, Module.ACTION_MANAGEMENT, PermissionType.READ)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> getById(@PathVariable Long id, Authentication auth) {
        if (!hasPermission(auth, Module.ACTION_MANAGEMENT, PermissionType.READ)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ActionRecordDTO> create(@RequestBody ActionRecordDTO dto, Authentication auth) {
        if (!hasPermission(auth, Module.ACTION_MANAGEMENT, PermissionType.CREATE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> update(@PathVariable Long id,
                                                  @RequestBody ActionRecordDTO dto,
                                                  Authentication auth) {
        if (!hasPermission(auth, Module.ACTION_MANAGEMENT, PermissionType.UPDATE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        if (!hasPermission(auth, Module.ACTION_MANAGEMENT, PermissionType.DELETE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
