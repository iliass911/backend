package com.sebn.brettbau.domain.role.controller;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    // Create a new role => require CREATE on ROLE
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestParam String roleName) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE roles.");
        }
        Role newRole = roleService.createRole(roleName);
        return ResponseEntity.ok(newRole);
    }

    // Assign permission => require UPDATE on ROLE
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<String> assignPermission(
        @PathVariable Long roleId,
        @RequestParam Module module,
        @RequestParam PermissionType permissionType
    ) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE roles.");
        }
        roleService.assignPermissionToRole(roleId, module, permissionType);
        return ResponseEntity.ok("Permission assigned successfully");
    }

    // List all roles => require READ on ROLE
    @GetMapping
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ roles.");
        }
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Get role by ID => require READ on ROLE
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ roles.");
        }
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }
}
