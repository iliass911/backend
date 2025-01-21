package com.sebn.brettbau.domain.user.controller;

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
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") 
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // For listing all users => require READ on USER
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.READ)) {
            throw new AccessDeniedException("You do not have permission to READ users.");
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // For single user => also require READ on USER
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ users.");
        }
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // If you want an endpoint to assign role to user => require UPDATE on USER
    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE user roles.");
        }
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }
}
