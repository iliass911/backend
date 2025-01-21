// src/main/java/com/example/backend/domain/audit/dto/AuditLogDTO.java
package com.sebn.brettbau.domain.audit.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private String action;
    private String details;
    private String entityType;
    private Long entityId;
    private String username;
    private String matricule;
    private LocalDateTime timestamp;
    private String ipAddress;
}