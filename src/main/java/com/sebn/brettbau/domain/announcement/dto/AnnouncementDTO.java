// src/main/java/com/sebn/brettbau/domain/announcement/dto/AnnouncementDTO.java
package com.sebn.brettbau.domain.announcement.dto;

import com.sebn.brettbau.domain.security.Module;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String message;
    private String creatorUsername;
    private boolean isAnonymous;
    private Set<Module> targetModules;
    private LocalDateTime createdAt;
}
