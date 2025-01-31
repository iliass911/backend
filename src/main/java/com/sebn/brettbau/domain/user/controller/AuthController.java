package com.sebn.brettbau.domain.user.controller;

import com.sebn.brettbau.config.JwtTokenUtil;
import com.sebn.brettbau.domain.user.dto.AuthResponse;
import com.sebn.brettbau.domain.user.dto.LoginRequest;
import com.sebn.brettbau.domain.user.dto.RegisterRequest;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private RoleService roleService;

    /**
     * Retrieves permissions for a given role.
     *
     * @param role The role for which permissions are retrieved.
     * @return A map of modules to their respective permissions.
     */
    private Map<Module, Set<String>> getPermissionsForRole(Role role) {
        Map<Module, Set<String>> permissions = new HashMap<>();
        
        for (Module module : Module.values()) {
            Set<String> modulePermissions = new HashSet<>();
            for (PermissionType permType : PermissionType.values()) {
                if (roleService.roleHasPermission(role, module, permType)) {
                    modulePermissions.add(permType.name());
                }
            }
            if (!modulePermissions.isEmpty()) {
                permissions.put(module, modulePermissions);
            }
        }
        
        return permissions;
    }

    /**
     * Registers a new user.
     *
     * @param registerRequest The registration request containing user details.
     * @return An authentication response with JWT token and user details.
     * @throws Exception If registration fails.
     */
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        User user = userService.registerUser(registerRequest);
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        Map<Module, Set<String>> permissions = getPermissionsForRole(user.getRole());
        return new AuthResponse(token, user.getRole().getName(), user.getId(), permissions);
    }

    /**
     * Authenticates a user and provides a JWT token.
     *
     * @param loginRequest The login request containing username and password.
     * @return An authentication response with JWT token and user details.
     * @throws Exception If authentication fails.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        User user = userService.authenticateUser(loginRequest);
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        Map<Module, Set<String>> permissions = getPermissionsForRole(user.getRole());
        return new AuthResponse(token, user.getRole().getName(), user.getId(), permissions);
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtTokenUtil.validateToken(token.substring(7));
        }
        return false;
    }
}

