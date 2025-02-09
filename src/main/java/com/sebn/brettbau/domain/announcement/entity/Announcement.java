// src/main/java/com/sebn/brettbau/domain/announcement/entity/Announcement.java
package com.sebn.brettbau.domain.announcement.entity;

import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.security.Module;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    private boolean isAnonymous;

    @ElementCollection
    @CollectionTable(name = "announcement_target_modules")
    @Column(name = "module")
    @Enumerated(EnumType.STRING)
    private Set<Module> targetModules;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}