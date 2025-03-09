package com.sebn.brettbau.domain.chat.service;

import com.sebn.brettbau.domain.chat.dto.ChatMessageDTO;
import com.sebn.brettbau.domain.chat.dto.ChatMessageRequest;
import com.sebn.brettbau.domain.chat.entity.ChatMessage;
import com.sebn.brettbau.domain.chat.entity.ChatMention;
import com.sebn.brettbau.domain.chat.repository.ChatMessageRepository;
import com.sebn.brettbau.domain.notification.service.NotificationService;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getAllMessages() {
        try {
            return chatMessageRepository.findAllByOrderByCreatedAtDesc().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all messages: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch messages", e);
        }
    }

    @Transactional
    public ChatMessageDTO createMessage(ChatMessageRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            Set<User> mentionedUsers = new HashSet<>();

            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

            log.debug("Creating message for user: {}", currentUser.getUsername());
            log.debug("Message content: {}", request.getContent());

            // Handle @everyone mention: no individual mention records, only notifications.
            if (request.isEveryoneMention()) {
                if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.DELETE)) {
                    throw new AccessDeniedException("Only users with delete permission can mention @everyone");
                }
                log.debug("@everyone mention activated");
            } 
            // Handle individual mentions if not @everyone
            else if (request.getMentionedUsernames() != null && !request.getMentionedUsernames().isEmpty()) {
                for (String username : request.getMentionedUsernames()) {
                    try {
                        userService.findByUsername(username).ifPresent(user -> {
                            mentionedUsers.add(user);
                            log.debug("Added mention for user: {}", username);
                        });
                    } catch (Exception e) {
                        log.error("Error processing mentioned user {}: {}", username, e.getMessage());
                    }
                }
            }

            // Create the initial chat message
            ChatMessage message = ChatMessage.builder()
                    .content(request.getContent())
                    .sender(currentUser)
                    .createdAt(LocalDateTime.now())
                    .isEveryoneMention(request.isEveryoneMention())
                    .mentions(new HashSet<>())  // initialize empty set
                    .hasAttachment(false)
                    .build();

            final ChatMessage savedMessage = chatMessageRepository.save(message);
            log.debug("Saved initial message with ID: {}", savedMessage.getId());

            // For individual mentions, create mention records and notifications.
            if (!request.isEveryoneMention() && !mentionedUsers.isEmpty()) {
                try {
                    Set<ChatMention> mentions = mentionedUsers.stream()
                            .filter(user -> !user.getId().equals(currentUser.getId()))
                            .map(user -> ChatMention.builder()
                                    .message(savedMessage)
                                    .mentionedUser(user)
                                    .build())
                            .collect(Collectors.toSet());
                    savedMessage.setMentions(mentions);
                    final ChatMessage messageWithMentions = chatMessageRepository.save(savedMessage);
                    log.debug("Added {} mention records to message", mentions.size());
                    createMentionNotifications(messageWithMentions, mentionedUsers);
                    return mapToDTO(messageWithMentions);
                } catch (Exception e) {
                    log.error("Error processing mentions: {}", e.getMessage());
                    return mapToDTO(savedMessage);
                }
            } 
            // For @everyone, send notifications to all users except the sender.
            else if (request.isEveryoneMention()) {
                Set<User> allUsers = new HashSet<>(userService.getAllUsers());
                allUsers.removeIf(user -> user.getId().equals(currentUser.getId()));
                createMentionNotifications(savedMessage, allUsers);
            }
            return mapToDTO(savedMessage);
        } catch (Exception e) {
            log.error("Error creating message: {}", e.getMessage());
            throw new RuntimeException("Failed to create chat message", e);
        }
    }

    @Transactional
    public ChatMessageDTO updateMessage(Long messageId, ChatMessageRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            ChatMessage message = chatMessageRepository.findById(messageId)
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

            if (!message.getSender().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You can only edit your own messages");
            }

            message.setContent(request.getContent());
            message.setUpdatedAt(LocalDateTime.now());
            
            final ChatMessage updatedMessage = chatMessageRepository.save(message);
            log.debug("Updated message with ID: {}", messageId);
            
            return mapToDTO(updatedMessage);
        } catch (Exception e) {
            log.error("Error updating message {}: {}", messageId, e.getMessage());
            throw new RuntimeException("Failed to update message", e);
        }
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        try {
            User currentUser = userService.getCurrentUser();
            ChatMessage message = chatMessageRepository.findById(messageId)
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

            boolean hasDeletePermission = roleService.roleHasPermission(
                    currentUser.getRole(), Module.CHAT, PermissionType.DELETE);

            if (!message.getSender().getId().equals(currentUser.getId()) && !hasDeletePermission) {
                throw new AccessDeniedException("You can only delete your own messages or need delete permission");
            }

            chatMessageRepository.delete(message);
            log.debug("Deleted message with ID: {}", messageId);
        } catch (Exception e) {
            log.error("Error deleting message {}: {}", messageId, e.getMessage());
            throw new RuntimeException("Failed to delete message", e);
        }
    }

    private ChatMessageDTO mapToDTO(ChatMessage message) {
        try {
            ChatMessageDTO dto = new ChatMessageDTO();
            dto.setId(message.getId());
            dto.setContent(message.getContent());
            
            Hibernate.initialize(message.getSender());
            dto.setSenderUsername(message.getSender().getUsername());
            dto.setSenderRole(message.getSender().getRole().getName());
            
            dto.setCreatedAt(message.getCreatedAt());
            dto.setUpdatedAt(message.getUpdatedAt());
            dto.setEveryoneMention(message.isEveryoneMention());
            dto.setHasAttachment(message.isHasAttachment());
            dto.setAttachmentUrl(message.getAttachmentUrl());
            dto.setAttachmentType(message.getAttachmentType());
            dto.setAttachmentName(message.getAttachmentName());

            Set<String> mentionedUsernames = new HashSet<>();
            if (message.getMentions() != null) {
                Hibernate.initialize(message.getMentions());
                mentionedUsernames = message.getMentions().stream()
                        .map(mention -> {
                            Hibernate.initialize(mention.getMentionedUser());
                            return mention.getMentionedUser().getUsername();
                        })
                        .collect(Collectors.toSet());
            }
            dto.setMentionedUsernames(mentionedUsernames);

            return dto;
        } catch (Exception e) {
            log.error("Error mapping message to DTO: {}", e.getMessage());
            throw new RuntimeException("Failed to map message to DTO", e);
        }
    }

    private void createMentionNotifications(ChatMessage message, Set<User> mentionedUsers) {
        String senderName = message.getSender().getUsername();
        String notificationType = message.isEveryoneMention() ? "EVERYONE_MENTION" : "MENTION";
        String content = message.isEveryoneMention() 
            ? String.format("%s mentioned everyone: %s", senderName, message.getContent())
            : String.format("%s mentioned you: %s", senderName, message.getContent());

        mentionedUsers.stream()
            .filter(user -> !user.getId().equals(message.getSender().getId()))
            .forEach(mentionedUser -> {
                try {
                    notificationService.createNotification(
                        "Chat Mention",
                        content,
                        "CHAT",
                        notificationType,
                        "LOW",
                        mentionedUser.getId(),
                        message.getId(),
                        "CHAT_MESSAGE"
                    );
                    log.debug("Created notification for user: {}", mentionedUser.getUsername());
                } catch (Exception e) {
                    log.error("Error creating notification for user {}: {}", 
                        mentionedUser.getUsername(), e.getMessage());
                }
            });
    }
}
