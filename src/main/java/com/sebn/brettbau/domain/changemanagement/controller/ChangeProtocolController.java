// src/main/java/com/example/backend/domain/changemanagement/controller/ChangeProtocolController.java
package com.sebn.brettbau.domain.changemanagement.controller;

import com.sebn.brettbau.domain.changemanagement.dto.ChangeProtocolDTO;
import com.sebn.brettbau.domain.changemanagement.service.ChangeProtocolService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/changeprotocols")
@RequiredArgsConstructor
@CrossOrigin
public class ChangeProtocolController {
    private final ChangeProtocolService service;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ChangeProtocolDTO> create(@RequestBody ChangeProtocolDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHANGE_PROTOCOL, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE change protocols.");
        }
        ChangeProtocolDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeProtocolDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHANGE_PROTOCOL, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ change protocols.");
        }
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ChangeProtocolDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHANGE_PROTOCOL, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ change protocols.");
        }
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChangeProtocolDTO> update(@PathVariable Long id, @RequestBody ChangeProtocolDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHANGE_PROTOCOL, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE change protocols.");
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHANGE_PROTOCOL, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE change protocols.");
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
