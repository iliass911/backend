package com.example.backend.config;

import com.example.backend.domain.role.entity.Permission;
import com.example.backend.domain.role.entity.Role;
import com.example.backend.domain.role.repository.PermissionRepository;
import com.example.backend.domain.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // Only initialize if no permissions exist
        if (permissionRepository.count() == 0) {
            // Create a list of default permissions using the new constructor
            List<Permission> defaultPermissions = Arrays.asList(
                // Audit permissions
                new Permission("AUDIT_VIEW", "AUDIT", "VIEW", "View audit logs"),

                // Inventory permissions
                new Permission("INVENTORY_VIEW", "INVENTORY", "VIEW", "View inventory"),
                new Permission("INVENTORY_CREATE", "INVENTORY", "CREATE", "Create inventory"),
                new Permission("INVENTORY_UPDATE", "INVENTORY", "UPDATE", "Update inventory"),
                new Permission("INVENTORY_DELETE", "INVENTORY", "DELETE", "Delete inventory"),

                // BOM permissions
                new Permission("BOM_VIEW", "BOM", "VIEW", "View BOM"),
                new Permission("BOM_CREATE", "BOM", "CREATE", "Create BOM"),
                new Permission("BOM_UPDATE", "BOM", "UPDATE", "Update BOM"),
                new Permission("BOM_DELETE", "BOM", "DELETE", "Delete BOM"),

                // Maintenance permissions
                new Permission("MAINTENANCE_VIEW", "MAINTENANCE", "VIEW", "View maintenance"),
                new Permission("MAINTENANCE_CREATE", "MAINTENANCE", "CREATE", "Create maintenance"),
                new Permission("MAINTENANCE_UPDATE", "MAINTENANCE", "UPDATE", "Update maintenance"),
                new Permission("MAINTENANCE_DELETE", "MAINTENANCE", "DELETE", "Delete maintenance")
                
                // Add all your other permissions similarly...
            );

            // Save permissions to the repository
            List<Permission> savedPermissions = permissionRepository.saveAll(defaultPermissions);

            // Create ADMIN role with all permissions if it doesn't exist
            roleRepository.findByName("ADMIN").ifPresentOrElse(
                existing -> { /* Do nothing if ADMIN role exists */ },
                () -> {
                    Role adminRole = new Role();
                    adminRole.setName("ADMIN");
                    adminRole.setDescription("Administrator with full access");
                    adminRole.setPermissions(new HashSet<>(savedPermissions));
                    roleRepository.save(adminRole);
                }
            );

            // Create USER role with basic VIEW permissions if it doesn't exist
            roleRepository.findByName("USER").ifPresentOrElse(
                existing -> { /* Do nothing if USER role exists */ },
                () -> {
                    Role userRole = new Role();
                    userRole.setName("USER");
                    userRole.setDescription("Regular user with limited access");
                    
                    // Filter VIEW permissions for USER role
                    Set<Permission> userPermissions = savedPermissions.stream()
                        .filter(p -> "VIEW".equals(p.getAction()))
                        .collect(Collectors.toSet());
                    
                    userRole.setPermissions(userPermissions);
                    roleRepository.save(userRole);
                }
            );
        }
    }
}
