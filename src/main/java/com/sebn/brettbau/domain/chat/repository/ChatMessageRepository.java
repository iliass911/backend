// src/main/java/com/sebn/brettbau/domain/chat/repository/ChatMessageRepository.java
package com.sebn.brettbau.domain.chat.repository;

import com.sebn.brettbau.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByOrderByCreatedAtDesc();
}