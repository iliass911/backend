// RoleController.java
package com.example.backend.domain.role.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.role.dto.CreateRoleRequest;
import com.example.backend.domain.role.entity.Permission;
import com.example.backend.domain.role.entity.Role;
import com.example.backend.domain.role.repository.PermissionRepository;
import com.example.backend.domain.role.service.PermissionChecker;
import com.example.backend.domain.role.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends BaseController {
    private final RoleService roleService;
    private final PermissionRepository permissionRepository;

    public RoleController(PermissionChecker permissionChecker, RoleService roleService, PermissionRepository permissionRepository) {
        super(permissionChecker);
        this.roleService = roleService;
        this.permissionRepository = permissionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        checkPermission("ROLE", "VIEW");
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequest request) {
        checkPermission("ROLE", "CREATE");
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        checkPermission("ROLE", "DELETE");
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(
        @PathVariable Long id,
        @RequestBody CreateRoleRequest request
    ) {
        checkPermission("ROLE", "UPDATE");
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        checkPermission("PERMISSION", "VIEW");
        return ResponseEntity.ok(permissionRepository.findAll());
    }
}