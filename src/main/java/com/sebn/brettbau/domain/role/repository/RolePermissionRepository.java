package com.sebn.brettbau.domain.role.repository;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleAndModuleAndPermissionType(Role role, Module module, PermissionType permissionType);
    void deleteByRoleAndModuleAndPermissionType(Role role, Module module, PermissionType permissionType);
}
