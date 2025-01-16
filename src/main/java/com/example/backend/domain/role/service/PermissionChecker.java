package com.example.backend.domain.role.service;

import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.user.service.UserService;
import com.example.backend.domain.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionChecker {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public boolean hasPermission(String resource, String action) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null || currentUser.getRole() == null) {
            return false;
        }
        
        return roleRepository.findByName(currentUser.getRole())
            .map(role -> role.getPermissions().stream()
                .anyMatch(p -> p.getResource().equals(resource) 
                    && p.getAction().equals(action)))
            .orElse(false);
    }
}
