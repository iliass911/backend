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

/**
 * REST controller for managing roles.
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://10.150.2.201:3000")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RoleController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Creates a new role.
     * Requires CREATE permission on the ROLE module.
     *
     * @param roleName the name of the new role
     * @return ResponseEntity containing the created role
     */
    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestParam String roleName) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE roles.");
        }
        Role newRole = roleService.createRole(roleName);
        return ResponseEntity.ok(newRole);
    }

    /**
     * Assigns a permission to a role.
     * Requires UPDATE permission on the ROLE module.
     */
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

    /**
     * Removes a permission from a role.
     * Requires DELETE permission on the ROLE module.
     */
    @DeleteMapping("/{roleId}/permissions")
    public ResponseEntity<String> removePermission(
            @PathVariable Long roleId,
            @RequestParam Module module,
            @RequestParam PermissionType permissionType
    ) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE role permissions.");
        }
        roleService.removePermissionFromRole(roleId, module, permissionType);
        return ResponseEntity.ok("Permission removed successfully");
    }

    /**
     * Retrieves all roles.
     * Requires READ permission on the ROLE module.
     */
    @GetMapping
    public ResponseEntity<Iterable<Role>> getAllRoles() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ roles.");
        }
        Iterable<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Retrieves a role by its ID.
     * Requires READ permission on the ROLE module.
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ roles.");
        }
        Role role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    // ------------------------------------------------------------------
    // NEW ENDPOINT: DELETE a role by ID (Requires DELETE permission)
    // ------------------------------------------------------------------
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ROLE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE roles.");
        }
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }
}
