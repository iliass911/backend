package com.sebn.brettbau.domain.notification.mapper;

import com.sebn.brettbau.domain.notification.dto.NotificationDTO;
import com.sebn.brettbau.domain.notification.entity.Notification;

public class NotificationMapper {
    public static NotificationDTO toDTO(Notification entity) {
        if (entity == null) return null;
        
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setModule(entity.getModule());
        dto.setType(entity.getType());
        dto.setSeverity(entity.getSeverity());
        dto.setReferenceId(entity.getReferenceId());
        dto.setReferenceType(entity.getReferenceType());
        dto.setRead(entity.isRead());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setReadAt(entity.getReadAt());
        return dto;
    }
}
