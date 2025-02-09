// src/main/java/com/sebn/brettbau/domain/chat/dto/ChatMessageDTO.java
package com.sebn.brettbau.domain.chat.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ChatMessageDTO {
    private Long id;
    private String content;
    private String senderUsername;
    private String senderRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isEveryoneMention;
    private Set<String> mentionedUsernames;
}