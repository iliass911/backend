// src/main/java/com/sebn/brettbau/domain/audit/controller/AuditLogController.java

package com.sebn.brettbau.domain.audit.controller;

import com.sebn.brettbau.domain.audit.entity.AuditLog;
import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(
    origins = "http://10.150.2.201:3000",
    allowedHeaders = {
        "Origin", 
        "Content-Type", 
        "Accept", 
        "Authorization",
        "X-Requesting-Module"
    }
)
@Validated
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService, RoleService roleService, UserService userService) {
        this.auditLogService = auditLogService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Retrieves all audit logs.
     * Requires READ permission on the AUDIT_LOGS module or indirect access through another permitted module.
     *
     * @param requestingModule Optional header indicating the requesting module.
     * @param page             Page number for pagination (default is 0).
     * @param size             Page size for pagination (default is 10).
     * @return ResponseEntity containing a page of AuditLog entities.
     */
    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.AUDIT_LOGS,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read audit logs.");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
            Page<AuditLog> auditLogs = auditLogService.getAuditLogs(pageable);
            return ResponseEntity.ok(auditLogs);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (you might want to use a logger here)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Retrieves audit logs within a specified date range.
     * Requires READ permission on the AUDIT_LOGS module or indirect access through another permitted module.
     *
     * @param requestingModule Optional header indicating the requesting module.
     * @param start            Start datetime for the range (ISO DATE_TIME format).
     * @param end              End datetime for the range (ISO DATE_TIME format).
     * @param page             Page number for pagination (default is 0).
     * @param size             Page size for pagination (default is 10).
     * @return ResponseEntity containing a page of AuditLog entities within the specified date range.
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByDateRange(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.AUDIT_LOGS,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read audit logs.");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
            Page<AuditLog> auditLogs = auditLogService.getAuditLogsByDateRange(start, end, pageable);
            return ResponseEntity.ok(auditLogs);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (you might want to use a logger here)
            return ResponseEntity.status(500).build();
        }
    }
}

