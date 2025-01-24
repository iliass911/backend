package com.sebn.brettbau.domain.role.service;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.role.repository.RoleRepository;
import com.sebn.brettbau.domain.role.repository.RolePermissionRepository;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Service
@Transactional
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private static final Map<Module, Set<Module>> INDIRECT_ACCESS_MAP = new EnumMap<>(Module.class);

    static {
        INDIRECT_ACCESS_MAP.put(Module.INVENTORY, new HashSet<>(Set.of(Module.SITE)));
        INDIRECT_ACCESS_MAP.put(Module.MAINTENANCE, new HashSet<>(Set.of(Module.SITE, Module.BOARD)));
    }

    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Role createRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role name already exists.");
        }
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public void assignPermissionToRole(Long roleId, Module module, PermissionType permissionType) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean exists = rolePermissionRepository.existsByRoleAndModuleAndPermissionType(role, module, permissionType);
        if (!exists) {
            RolePermission rp = RolePermission.builder()
                    .role(role)
                    .module(module)
                    .permissionType(permissionType)
                    .build();
            rolePermissionRepository.save(rp);
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

    public boolean hasPermissionOrIndirectAccess(Role role, Module targetModule, 
            PermissionType permissionType, Module requestingModule) {
        if (roleHasPermission(role, targetModule, permissionType)) {
            return true;
        }
        if (requestingModule != null) {
            boolean hasModulePermission = roleHasPermission(role, requestingModule, PermissionType.READ);
            Set<Module> allowedModules = INDIRECT_ACCESS_MAP.get(requestingModule);
            return hasModulePermission && allowedModules != null && allowedModules.contains(targetModule);
        }
        return false;
    }

    public boolean canModuleAccessOtherModule(Module accessingModule, Module targetModule) {
        Set<Module> allowedModules = INDIRECT_ACCESS_MAP.get(accessingModule);
        return allowedModules != null && allowedModules.contains(targetModule);
    }
}