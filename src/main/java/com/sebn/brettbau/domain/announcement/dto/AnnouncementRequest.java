// src/main/java/com/sebn/brettbau/domain/announcement/dto/AnnouncementRequest.java
package com.sebn.brettbau.domain.announcement.dto;

import com.sebn.brettbau.domain.security.Module;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class AnnouncementRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private boolean isAnonymous;
    
    @NotEmpty(message = "At least one target module must be selected")
    private Set<Module> targetModules;
}
