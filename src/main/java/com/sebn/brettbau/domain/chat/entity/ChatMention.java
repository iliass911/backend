// src/main/java/com/sebn/brettbau/domain/chat/entity/ChatMention.java
package com.sebn.brettbau.domain.chat.entity;

import com.sebn.brettbau.domain.user.entity.User;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "chat_mentions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentioned_user_id")
    private User mentionedUser;
}