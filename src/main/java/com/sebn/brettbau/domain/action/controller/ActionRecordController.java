package com.sebn.brettbau.domain.action.controller;

import com.sebn.brettbau.domain.action.dto.ActionRecordDTO;
import com.sebn.brettbau.domain.action.service.ActionRecordService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {
    "Origin",
    "Content-Type",
    "Accept",
    "Authorization",
    "X-Requesting-Module"
})
@RequiredArgsConstructor
public class ActionRecordController {

    private final ActionRecordService service;
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ActionRecordDTO>> getAllActions(
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.ACTION_MANAGEMENT);
            List<ActionRecordDTO> records = service.getAll();
            return ResponseEntity.ok(records);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> getById(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.READ, requestingModule, Module.ACTION_MANAGEMENT);
            ActionRecordDTO dto = service.getById(id);
            return ResponseEntity.ok(dto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ActionRecordDTO> createAction(
        @RequestBody ActionRecordDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.CREATE, requestingModule, Module.ACTION_MANAGEMENT);
            ActionRecordDTO created = service.create(dto);
            return ResponseEntity.ok(created);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> updateAction(
        @PathVariable Long id,
        @RequestBody ActionRecordDTO dto,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.UPDATE, requestingModule, Module.ACTION_MANAGEMENT);
            ActionRecordDTO updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(
        @PathVariable Long id,
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            verifyPermission(PermissionType.DELETE, requestingModule, Module.ACTION_MANAGEMENT);
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper method to parse X-Requesting-Module and call roleService.
     */
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

