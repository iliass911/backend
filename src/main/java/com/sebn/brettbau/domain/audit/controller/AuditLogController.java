package com.sebn.brettbau.domain.audit.controller;

import com.sebn.brettbau.domain.audit.entity.AuditLog;
import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@CrossOrigin
public class AuditLogController {
    
    private final AuditLogService auditLogService;
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Internal-Call", required = false) String internalCall) {
            
        // Skip permission check for internal calls
        if (internalCall == null) {
            User currentUser = userService.getCurrentUser();
            if (!roleService.roleHasPermission(currentUser.getRole(), Module.AUDIT, PermissionType.READ)) {
                throw new AccessDeniedException("You do not have permission to READ audit logs.");
            }
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(auditLogService.getAuditLogs(pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Internal-Call", required = false) String internalCall) {
            
        // Skip permission check for internal calls
        if (internalCall == null) {
            User currentUser = userService.getCurrentUser();
            if (!roleService.roleHasPermission(currentUser.getRole(), Module.AUDIT, PermissionType.READ)) {
                throw new AccessDeniedException("You do not have permission to READ audit logs.");
            }
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(auditLogService.getAuditLogsByDateRange(start, end, pageable));
    }
}