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
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private RoleService roleService;

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

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) throws Exception {
        User user = userService.registerUser(registerRequest);
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        Map<Module, Set<String>> permissions = getPermissionsForRole(user.getRole());
        return new AuthResponse(token, user.getRole().getName(), user.getId(), permissions);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        User user = userService.authenticateUser(loginRequest);
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().getName());
        Map<Module, Set<String>> permissions = getPermissionsForRole(user.getRole());
        return new AuthResponse(token, user.getRole().getName(), user.getId(), permissions);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return jwtTokenUtil.validateToken(token.substring(7));
        }
        return false;
    }
}