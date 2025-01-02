// src/main/java/com/example/backend/domain/user/service/UserService.java
package com.example.backend.domain.user.service;

import com.example.backend.domain.audit.service.AuditLogService;
import com.example.backend.domain.user.dto.LoginRequest; 
import com.example.backend.domain.user.dto.RegisterRequest;
import com.example.backend.domain.user.dto.PasswordChangeRequest; // Import the DTO
import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.user.repository.UserRepository;
import com.example.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    
    private final Set<String> allowedMatricules = new HashSet<>(Arrays.asList("90940", "23", "4501", "90948"));

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
        if (!allowedMatricules.contains(registerRequest.getMatricule())) {
            throw new Exception("Matricule not allowed to register.");
        }

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new Exception("Username already exists.");
        }

        String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());
        String role = registerRequest.getMatricule().equals("90940") ? "ADMIN" : "USER";

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(hashedPassword)
                .matricule(registerRequest.getMatricule())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        
        // Log registration event
        auditLogService.logEvent(
            savedUser,
            "USER_REGISTRATION",
            String.format("New user registered with matricule: %s and role: %s", 
                savedUser.getMatricule(), savedUser.getRole()),
            "USER",
            savedUser.getId()
        );

        return savedUser;
    }

    public User authenticateUser(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new Exception("Invalid username or password."));

        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            auditLogService.logEvent(
                user,
                "LOGIN_FAILED",
                String.format("Failed login attempt for user: %s", user.getUsername()),
                "USER",
                user.getId()
            );
            throw new Exception("Invalid username or password.");
        }

        // Log successful login
        auditLogService.logEvent(
            user,
            "LOGIN_SUCCESS",
            String.format("User logged in successfully: %s", user.getUsername()),
            "USER",
            user.getId()
        );

        return user;
    }

    public List<User> getAllUsers() {
        User currentUser = getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new ResourceNotFoundException("Access denied: Admin privileges required");
        }
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        User currentUser = getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
            throw new ResourceNotFoundException("Access denied: Cannot access other user's information");
        }
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public void addAllowedMatricule(String matricule) {
        User currentUser = getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new ResourceNotFoundException("Access denied: Admin privileges required");
        }
        allowedMatricules.add(matricule);
        
        auditLogService.logEvent(
            currentUser,
            "MATRICULE_ADDED",
            String.format("Added new allowed matricule: %s", matricule),
            "SYSTEM",
            null
        );
    }

    public void removeAllowedMatricule(String matricule) {
        User currentUser = getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new ResourceNotFoundException("Access denied: Admin privileges required");
        }
        allowedMatricules.remove(matricule);
        
        auditLogService.logEvent(
            currentUser,
            "MATRICULE_REMOVED", 
            String.format("Removed matricule from allowed list: %s", matricule),
            "SYSTEM",
            null
        );
    }
    
    // Add the changePassword method as per user instruction
    // Make sure to import: import org.springframework.security.crypto.bcrypt.BCrypt;
    public void changePassword(PasswordChangeRequest request) {
        User currentUser = getCurrentUser();
        
        if (!BCrypt.checkpw(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        String newHashedPassword = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
        currentUser.setPassword(newHashedPassword);
        userRepository.save(currentUser);
        
        auditLogService.logEvent(
            currentUser,
            "PASSWORD_CHANGE",
            "User changed their password",
            "USER",
            currentUser.getId()
        );
    }
}
