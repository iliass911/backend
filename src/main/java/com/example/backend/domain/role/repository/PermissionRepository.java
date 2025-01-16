package com.example.backend.domain.role.repository;

import com.example.backend.domain.role.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}