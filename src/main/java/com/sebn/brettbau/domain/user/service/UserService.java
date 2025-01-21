package com.sebn.brettbau.domain.user.service;

import com.sebn.brettbau.domain.audit.service.AuditLogService; 
import com.sebn.brettbau.domain.role.entity.Role;
import com.sebn.brettbau.domain.role.repository.RoleRepository;
import com.sebn.brettbau.domain.user.dto.LoginRequest; 
import com.sebn.brettbau.domain.user.dto.RegisterRequest;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final AuditLogService auditLogService; // If you have an audit log service

   // If you want to restrict registration to certain matricules:
   // private final Set<String> allowedMatricules = Set.of("90940", "23", "4501", "90948");

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
       // If you want to restrict certain matricules:
       // if (!allowedMatricules.contains(registerRequest.getMatricule())) {
       //     throw new Exception("Matricule not allowed to register.");
       // }

       if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
           throw new Exception("Username already exists.");
       }

       String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());
       // By default, let's attach them to the "USER" role:
       Role defaultRole = roleRepository.findByName("USER")
               .orElseThrow(() -> new Exception("Role [USER] not found in DB"));

       User user = User.builder()
               .username(registerRequest.getUsername())
               .password(hashedPassword)
               .matricule(registerRequest.getMatricule())
               .role(defaultRole)
               .build();

       User savedUser = userRepository.save(user);

       // If you have an audit log:
       if (auditLogService != null) {
           auditLogService.logEvent(
               savedUser,
               "USER_REGISTRATION",
               String.format("New user registered: %s", savedUser.getUsername()),
               "USER",
               savedUser.getId()
           );
       }

       return savedUser;
   }

   public User authenticateUser(LoginRequest loginRequest) throws Exception {
       User user = userRepository.findByUsername(loginRequest.getUsername())
           .orElseThrow(() -> new Exception("Invalid username or password."));

       if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
           if (auditLogService != null) {
               auditLogService.logEvent(
                   user,
                   "LOGIN_FAILED",
                   String.format("Failed login attempt for user: %s", user.getUsername()),
                   "USER",
                   user.getId()
               );
           }
           throw new Exception("Invalid username or password.");
       }

       // Optional: log successful login
       if (auditLogService != null) {
           auditLogService.logEvent(
               user,
               "LOGIN_SUCCESS",
               String.format("User logged in successfully: %s", user.getUsername()),
               "USER",
               user.getId()
           );
       }

       return user;
   }

   public List<User> getAllUsers() {
       return userRepository.findAll();
   }

   public User getUserById(Long id) {
       return userRepository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
   }

   /**
    * Simple method to assign a new role to an existing user.
    */
   public void assignRoleToUser(Long userId, Long roleId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

       Role role = roleRepository.findById(roleId)
               .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));

       user.setRole(role);
       userRepository.save(user);

       // Optional: audit log
       if (auditLogService != null) {
           auditLogService.logEvent(
               user,
               "ASSIGN_ROLE",
               String.format("Assigned role %s to user %s", role.getName(), user.getUsername()),
               "USER",
               user.getId()
           );
       }
   }
}
