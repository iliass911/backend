// src/main/java/com/example/backend/domain/audit/service/AuditLogService.java
package com.sebn.brettbau.domain.audit.service;

import com.sebn.brettbau.domain.audit.entity.AuditLog;
import com.sebn.brettbau.domain.audit.repository.AuditLogRepository;
import com.sebn.brettbau.domain.user.entity.User;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;

    public void logEvent(User user, String action, String details, String entityType, Long entityId) {
        AuditLog auditLog = AuditLog.builder()
            .username(user.getUsername())
            .matricule(user.getMatricule())
            .action(action)
            .details(details)
            .entityType(entityType)
            .entityId(entityId)
            .timestamp(LocalDateTime.now())
            .ipAddress(getClientIp())
            .build();
            
        auditLogRepository.save(auditLog);
    }

    public Page<AuditLog> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<AuditLog> getAuditLogsByDateRange(
        LocalDateTime start, 
        LocalDateTime end, 
        Pageable pageable
    ) {
        return auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end, pageable);
    }

    private String getClientIp() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
