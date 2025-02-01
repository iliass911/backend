package com.sebn.brettbau.domain.notification.service;

import com.sebn.brettbau.domain.notification.dto.NotificationDTO;
import com.sebn.brettbau.domain.notification.entity.Notification;
import com.sebn.brettbau.domain.notification.mapper.NotificationMapper;
import com.sebn.brettbau.domain.notification.repository.NotificationRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final UserService userService;

    @Transactional
    public NotificationDTO createNotification(String title, String message, String module, 
                                           String type, String severity, Long userId,
                                           Long referenceId, String referenceType) {
        User user = userService.getUserById(userId);
        
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .module(module)
                .type(type)
                .severity(severity)
                .user(user)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        return NotificationMapper.toDTO(repository.save(notification));
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return repository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        return NotificationMapper.toDTO(repository.save(notification));
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = repository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
        LocalDateTime now = LocalDateTime.now();
        
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });
        
        repository.saveAll(unreadNotifications);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndRead(userId, false);
    }
}