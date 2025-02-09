// src/main/java/com/sebn/brettbau/domain/chat/dto/ChatMessageRequest.java
package com.sebn.brettbau.domain.chat.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class ChatMessageRequest {
    @NotBlank(message = "Message content is required")
    private String content;
    private Set<String> mentionedUsernames;
    private boolean isEveryoneMention;
}
