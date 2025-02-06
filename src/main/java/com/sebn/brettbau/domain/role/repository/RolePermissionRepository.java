package com.sebn.brettbau.domain.role.repository;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * Checks if a specific permission record exists for a role/module/permissionType combination.
     */
    boolean existsByRoleAndModuleAndPermissionType(Role role, Module module, PermissionType permissionType);

    /**
     * Deletes a specific permission record (by role, module, and permission type).
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp " +
           "WHERE rp.role = :role AND rp.module = :module AND rp.permissionType = :permissionType")
    void deleteByRoleAndModuleAndPermissionType(@Param("role") Role role,
                                                @Param("module") Module module,
                                                @Param("permissionType") PermissionType permissionType);

    /**
     * Deletes all permissions for a given role.
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role = :role")
    void deleteAllByRole(@Param("role") Role role);
}
