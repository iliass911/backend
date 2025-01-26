package com.sebn.brettbau.domain.role.service;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.role.repository.RoleRepository;
import com.sebn.brettbau.domain.role.repository.RolePermissionRepository;
import com.sebn.brettbau.domain.security.Module; // Import Module enum
import com.sebn.brettbau.domain.security.PermissionType; // Import PermissionType enum
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

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

    // Updated INDIRECT_ACCESS_MAP with PACK, SITE, PROJECT, BOM
    private static final Map<Module, Set<Module>> INDIRECT_ACCESS_MAP = new EnumMap<>(Module.class);

    static {
        INDIRECT_ACCESS_MAP.put(Module.INVENTORY, new HashSet<>(Set.of(Module.SITE)));
        INDIRECT_ACCESS_MAP.put(Module.MAINTENANCE, new HashSet<>(Set.of(Module.SITE, Module.BOARD)));
        INDIRECT_ACCESS_MAP.put(Module.PACK, new HashSet<>(Set.of(Module.SITE, Module.PROJECT)));
        INDIRECT_ACCESS_MAP.put(Module.MAINTENANCE_SCHEDULE, new HashSet<>(Set.of(Module.PACK, Module.SITE, Module.PROJECT)));
        INDIRECT_ACCESS_MAP.put(Module.USERS_PREVENTIVE, new HashSet<>(Set.of(Module.SITE, Module.PROJECT, Module.PACK, Module.BOARD, Module.MAINTENANCE_SCHEDULE)));
        INDIRECT_ACCESS_MAP.put(Module.BOM, new HashSet<>(Set.of(Module.BOARD, Module.SITE, Module.PROJECT, Module.INVENTORY)));
        INDIRECT_ACCESS_MAP.put(Module.FB_FAMILY, new HashSet<>(Set.of(Module.BOARD, Module.SITE, Module.PROJECT, Module.INVENTORY, Module.BOM)));
        INDIRECT_ACCESS_MAP.put(Module.BOARD, new HashSet<>(Set.of(Module.PACK, Module.SITE, Module.PROJECT, Module.BOM)));
        INDIRECT_ACCESS_MAP.put(Module.ACTION_MANAGEMENT, new HashSet<>(Set.of(Module.SITE, Module.PROJECT, Module.PACK, Module.BOARD,Module.PHASE, Module.MAINTENANCE_SCHEDULE)));
    }

    @Autowired
    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    /**
     * Creates a new role with the given name.
     *
     * @param roleName the name of the role to create
     * @return the created Role entity
     * @throws IllegalArgumentException if the role name already exists
     */
    public Role createRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role name already exists.");
        }
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    /**
     * Assigns a permission to a role.
     *
     * @param roleId         the ID of the role
     * @param module         the module to assign the permission to
     * @param permissionType the type of permission
     */
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
            logger.info("Assigned {} permission on {} module to role {}", permissionType, module, role.getName());
        } else {
            logger.warn("Permission {} on {} module already exists for role {}", permissionType, module, role.getName());
        }
    }

    /**
     * Removes a permission from a role.
     *
     * @param roleId         the ID of the role
     * @param module         the module from which to remove the permission
     * @param permissionType the type of permission to remove
     */
    public void removePermissionFromRole(Long roleId, Module module, PermissionType permissionType) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        boolean removed = role.getPermissions().removeIf(permission ->
                permission.getModule() == module &&
                permission.getPermissionType() == permissionType
        );

        if (removed) {
            rolePermissionRepository.deleteByRoleAndModuleAndPermissionType(role, module, permissionType);
            logger.info("Removed {} permission on {} module from role {}", permissionType, module, role.getName());
        } else {
            logger.warn("Permission {} on {} module not found for role {}", permissionType, module, role.getName());
        }

        roleRepository.save(role);
    }

    /**
     * Checks if a role has a specific permission on a module.
     *
     * @param role           the role to check
     * @param module         the module in question
     * @param permissionType the type of permission
     * @return true if the role has the permission, false otherwise
     */
    public boolean roleHasPermission(Role role, Module module, PermissionType permissionType) {
        if (role == null) return false;
        return rolePermissionRepository.existsByRoleAndModuleAndPermissionType(role, module, permissionType);
    }

    /**
     * Retrieves a role by its ID.
     *
     * @param id the ID of the role
     * @return the Role entity
     * @throws RuntimeException if the role is not found
     */
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    /**
     * Retrieves a role by its name.
     *
     * @param name the name of the role
     * @return the Role entity or null if not found
     */
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElse(null);
    }

    /**
     * Retrieves all roles.
     *
     * @return an iterable of all Role entities
     */
    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Checks if a role has a specific permission on a target module,
     * considering indirect access through other modules.
     *
     * @param role            the role to check
     * @param targetModule    the target module
     * @param permissionType  the type of permission
     * @param requestingModule the module requesting access
     * @return true if access is granted, false otherwise
     */
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

    /**
     * Determines if one module can access another module based on indirect access rules.
     *
     * @param accessingModule the module attempting to access another module
     * @param targetModule    the module being accessed
     * @return true if access is allowed, false otherwise
     */
    public boolean canModuleAccessOtherModule(Module accessingModule, Module targetModule) {
        Set<Module> allowedModules = INDIRECT_ACCESS_MAP.get(accessingModule);
        return allowedModules != null && allowedModules.contains(targetModule);
    }
}
