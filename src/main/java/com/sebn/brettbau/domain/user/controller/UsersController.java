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

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Retrieves all users.
     * Requires READ permission on the USER module.
     *
     * @return ResponseEntity containing the list of users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.READ)) {
            throw new AccessDeniedException("You do not have permission to READ users.");
        }
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by ID.
     * Requires READ permission on the USER module.
     *
     * @param id the ID of the user
     * @return ResponseEntity containing the user
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ users.");
        }
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Assigns a role to a user.
     * Requires UPDATE permission on the USER module.
     *
     * @param userId the ID of the user
     * @param roleId the ID of the role to assign
     * @return ResponseEntity with HTTP status
     */
    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.USER, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE user roles.");
        }
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    // Additional endpoints can be added here as needed, ensuring appropriate permission checks.
}
