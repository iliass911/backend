package com.sebn.brettbau.domain.notification.repository;

import com.sebn.brettbau.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(Long userId, boolean read);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndRead(Long userId, boolean read);
}