package com.sebn.brettbau.domain.user.controller;

import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.dto.PasswordChangeRequest;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://10.150.2.201:3000")
public class UsersController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // --- Updated Endpoint: Get all Users with X-Requesting-Module header support ---
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        User currentUser = userService.getCurrentUser();

        // Parse the requestingModule header into a Module enum value (callingModule)
        Module callingModule = null;
        if (requestingModule != null) {
            try {
                callingModule = Module.valueOf(requestingModule.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
            }
        }

        // Use the extended permission check that allows for indirect access if applicable
        boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
            currentUser.getRole(),
            Module.USER,
            PermissionType.READ,
            callingModule
        );

        if (!hasAccess) {
            throw new AccessDeniedException("You do not have permission to READ users.");
        }

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ users.");
        }
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE user roles.");
        }
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    // --- New Endpoint: Update a User ---
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update users.");
        }
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // --- New Endpoint: Delete a User ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete users.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
        @RequestBody PasswordChangeRequest request,
        @RequestHeader("Authorization") String token
    ) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            User currentUser = userService.getCurrentUser();
            userService.changePassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok().body(new HashMap<String, String>() {{
                put("message", "Password updated successfully");
            }});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("message", e.getMessage());
            }});
        }
    }
}
