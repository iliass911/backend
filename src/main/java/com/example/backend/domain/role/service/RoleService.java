package com.example.backend.domain.role.service;

import com.example.backend.domain.role.dto.CreateRoleRequest;
import com.example.backend.domain.role.entity.Permission;
import com.example.backend.domain.role.entity.Role;
import com.example.backend.domain.role.repository.PermissionRepository;
import com.example.backend.domain.role.repository.RoleRepository;
import com.example.backend.exception.ResourceNotFoundException;  // Ensure you have this exception class defined
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createRole(CreateRoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = request.getPermissionIds().stream()
                .map(id -> permissionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found")))
                .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }
        
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Role updateRole(Long id, CreateRoleRequest request) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = request.getPermissionIds().stream()
                .map(permId -> permissionRepository.findById(permId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found")))
                .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }
        
        return roleRepository.save(role);
    }
}