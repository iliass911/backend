// UsersController.java
package com.example.backend.domain.user.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.role.dto.CreateUserRequest;
import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.user.service.UserService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController extends BaseController {
    private final UserService userService;

    public UsersController(PermissionChecker permissionChecker, UserService userService) {
        super(permissionChecker);
        this.userService = userService;  
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        checkPermission("USER", "VIEW");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        checkPermission("USER", "CREATE");
        return ResponseEntity.ok(userService.createUser(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        checkPermission("USER", "DELETE"); 
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateUserRole(
        @PathVariable Long id,  
        @RequestParam Long roleId
    ) {
        checkPermission("USER", "UPDATE");
        return ResponseEntity.ok(userService.updateUserRole(id, roleId));
    }
}