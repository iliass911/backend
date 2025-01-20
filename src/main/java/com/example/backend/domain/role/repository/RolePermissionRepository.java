// RolePermissionRepository.java
package com.example.backend.domain.role.repository;

import com.example.backend.domain.role.entity.Role;
import com.example.backend.domain.role.entity.RolePermission;
import com.example.backend.domain.security.Module;
import com.example.backend.domain.security.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleAndModuleAndPermissionType(Role role, Module module, PermissionType permissionType);
}
