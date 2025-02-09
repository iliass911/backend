// src/main/java/com/sebn/brettbau/domain/chat/repository/ChatMentionRepository.java
package com.sebn.brettbau.domain.chat.repository;

import com.sebn.brettbau.domain.chat.entity.ChatMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMentionRepository extends JpaRepository<ChatMention, Long> {
}