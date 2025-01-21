package com.sebn.brettbau.config;

import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.repository.RoleRepository;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1) Ensure "ADMIN" role
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole = roleRepository.save(adminRole);
        }

        // 2) Ensure "USER" role
        Role userRole = roleRepository.findByName("USER").orElse(null);
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            userRole = roleRepository.save(userRole);
        }

        // 3) Give ADMIN every possible permission
        for (Module m : Module.values()) {
            for (PermissionType p : PermissionType.values()) {
                if (!roleService.roleHasPermission(adminRole, m, p)) {
                    roleService.assignPermissionToRole(adminRole.getId(), m, p);
                }
            }
        }

        // 4) Create an admin user if not exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setMatricule("00000");
            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
        }
    }
}