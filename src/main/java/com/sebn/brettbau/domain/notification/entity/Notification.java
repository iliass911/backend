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
    private String module;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long referenceId;
    private String referenceType;

    @Column(name = "`read`", nullable = false)
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}