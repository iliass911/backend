package com.sebn.brettbau.domain.role.repository;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.entity.RolePermission;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
    /**
     * Find all distinct modules associated with a specific role ID
     * @param roleId The ID of the role to query
     * @return List of distinct module names
     */
    @Query("SELECT DISTINCT rp.module FROM RolePermission rp WHERE rp.role.id = ?1")
    List<String> findDistinctModulesByRoleId(Long roleId);
    
    /**
     * Check if a specific role permission exists
     * @param role The role to check
     * @param module The module to check
     * @param permissionType The type of permission to check
     * @return true if the permission exists, false otherwise
     */
    boolean existsByRoleAndModuleAndPermissionType(Role role, Module module, PermissionType permissionType);
}