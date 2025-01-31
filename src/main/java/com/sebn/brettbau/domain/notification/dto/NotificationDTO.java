package com.sebn.brettbau.domain.notification.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String module;
    private String type;
    private String severity;
    private Long referenceId;
    private String referenceType;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
