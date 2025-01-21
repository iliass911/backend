package com.sebn.brettbau.domain.role.service;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.role.repository.RoleRepository;
import com.sebn.brettbau.domain.role.repository.RolePermissionRepository;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Role createRole(String roleName) {
        // Maybe check if roleName already exists
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public void assignPermissionToRole(Long roleId, Module module, PermissionType permissionType) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Check if it already has that permission
        boolean exists = rolePermissionRepository.existsByRoleAndModuleAndPermissionType(role, module, permissionType);
        if (!exists) {
            RolePermission rp = RolePermission.builder()
                    .role(role)
                    .module(module)
                    .permissionType(permissionType)
                    .build();
            rolePermissionRepository.save(rp);

            // Optionally add it to role.getPermissions() if you want it loaded immediately:
            role.getPermissions().add(rp);
        }
    }

    public boolean roleHasPermission(Role role, Module module, PermissionType permissionType) {
        if (role == null) return false;
        return rolePermissionRepository.existsByRoleAndModuleAndPermissionType(role, module, permissionType);
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElse(null);
    }

    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
