package com.example.backend.domain.user.service;

import com.example.backend.domain.audit.service.AuditLogService;
import com.example.backend.domain.user.dto.LoginRequest;
import com.example.backend.domain.user.dto.RegisterRequest;
import com.example.backend.domain.role.dto.CreateUserRequest;
import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.role.entity.Role;
import com.example.backend.domain.user.repository.UserRepository;
import com.example.backend.domain.role.repository.RoleRepository;
import com.example.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    private static final String ADMIN_MATRICULE = "90940";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String DEFAULT_ROLE = "USER";

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User registerUser(RegisterRequest registerRequest) throws Exception {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String role = determineRole(registerRequest.getMatricule());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(hashedPassword)
                .matricule(registerRequest.getMatricule())
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public User authenticateUser(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // For the admin matricule, override the requested role with ADMIN
        String role = determineRole(request.getMatricule());
        
        // If it's the admin matricule, find the admin role, otherwise use the requested role
        Role roleEntity;
        if (ADMIN_MATRICULE.equals(request.getMatricule())) {
            roleEntity = roleRepository.findByName(ADMIN_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        } else {
            roleEntity = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .matricule(request.getMatricule())
            .role(role)
            .roleEntity(roleEntity)
            .build();

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // If the user has admin matricule, prevent role changes
        if (ADMIN_MATRICULE.equals(user.getMatricule())) {
            throw new RuntimeException("Cannot change role for admin matricule");
        }

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.setRole(role.getName());
        user.setRoleEntity(role);
        return userRepository.save(user);
    }

    private String determineRole(String matricule) {
        return ADMIN_MATRICULE.equals(matricule) ? ADMIN_ROLE : DEFAULT_ROLE;
    }
}