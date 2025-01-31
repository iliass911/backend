package com.sebn.brettbau.domain.notification.entity;

import com.sebn.brettbau.domain.user.entity.User;
import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private String module;  // e.g., "INVENTORY", "MAINTENANCE", etc.

    @Column(nullable = false)
    private String type;    // e.g., "WARNING", "ERROR", "INFO", "SUCCESS"

    @Column(nullable = false)
    private String severity; // "HIGH", "MEDIUM", "LOW"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;      // The user who should see this notification

    private Long referenceId;  // ID of the related entity (e.g., inventory_item_id)
    private String referenceType; // Type of the related entity (e.g., "INVENTORY_ITEM")

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}
