package com.sebn.brettbau.domain.role.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.role.repository.RolePermissionRepository;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PermissionController {
    private final RolePermissionRepository rolePermissionRepository;
    private final UserService userService; // To get current user's role ID

    @GetMapping("/permissions")
    public List<String> getPermissions(Authentication authentication) {
        User currentUser = userService.getCurrentUser();
        Long roleId = currentUser.getRole().getId();
        return rolePermissionRepository.findDistinctModulesByRoleId(roleId);
    }
}