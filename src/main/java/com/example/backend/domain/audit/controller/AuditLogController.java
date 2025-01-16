// AuditLogController.java
package com.example.backend.domain.audit.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.audit.entity.AuditLog;
import com.example.backend.domain.audit.service.AuditLogService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin
public class AuditLogController extends BaseController {
    private final AuditLogService auditLogService;
    
    public AuditLogController(PermissionChecker permissionChecker, AuditLogService auditLogService) {
        super(permissionChecker);
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        checkPermission("AUDIT", "VIEW");
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(auditLogService.getAuditLogs(pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        checkPermission("AUDIT", "VIEW");
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(auditLogService.getAuditLogsByDateRange(start, end, pageable));
    }
}