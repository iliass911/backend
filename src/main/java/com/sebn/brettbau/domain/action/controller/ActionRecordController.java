package com.sebn.brettbau.domain.action.controller;

import com.sebn.brettbau.domain.action.dto.ActionRecordDTO;
import com.sebn.brettbau.domain.action.service.ActionRecordService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionRecordController {

    private final ActionRecordService service;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<ActionRecordDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ACTION_MANAGEMENT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ action records.");
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ACTION_MANAGEMENT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ action records.");
        }
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ActionRecordDTO> create(@RequestBody ActionRecordDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ACTION_MANAGEMENT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE action records.");
        }
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionRecordDTO> update(@PathVariable Long id, @RequestBody ActionRecordDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ACTION_MANAGEMENT, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE action records.");
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ACTION_MANAGEMENT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE action records.");
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
