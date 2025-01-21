package com.sebn.brettbau.security;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionChecker")
public class PermissionChecker {

    private final RoleService roleService;
    private final UserRepository userRepository;

    public PermissionChecker(RoleService roleService, UserRepository userRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    public boolean hasPermission(Authentication authentication, Module module, PermissionType permissionType) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return false;
        Role role = user.getRole();
        return roleService.roleHasPermission(role, module, permissionType);
    }
}
