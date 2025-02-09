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
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getAllMessages() {
        return chatMessageRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageDTO createMessage(ChatMessageRequest request) {
        try {
            User currentUser = userService.getCurrentUser();
            Set<User> mentionedUsers = new HashSet<>();

            // Validate request content
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

            // Detailed logging for debugging mentions
            System.out.println("Current User: " + currentUser.getUsername());
            System.out.println("Mentioned Usernames: " + request.getMentionedUsernames());

            // Check if the request is trying to mention everyone
            if (request.isEveryoneMention()) {
                if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.DELETE)) {
                    throw new AccessDeniedException("Only users with delete permission can mention @everyone");
                }
                mentionedUsers.addAll(userService.getAllUsers());
            } else if (request.getMentionedUsernames() != null && !request.getMentionedUsernames().isEmpty()) {
                for (String username : request.getMentionedUsernames()) {
                    try {
                        User mentionedUser = userService.findByUsername(username)
                            .orElseThrow(() -> {
                                System.err.println("User not found: " + username);
                                return new ResourceNotFoundException("User not found: " + username);
                            });
                        mentionedUsers.add(mentionedUser);
                        System.out.println("Successfully found mentioned user: " + mentionedUser.getUsername());
                    } catch (Exception e) {
                        System.err.println("Error processing mentioned user " + username + ": " + e.getMessage());
                        throw e;
                    }
                }
            }

            // Logging for debugging purposes
            System.out.println("Creating message for user: " + currentUser.getUsername());
            System.out.println("Message content: " + request.getContent());
            System.out.println("Resolved mentioned users: " + mentionedUsers.stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList()));

            // Build the chat message
            ChatMessage message = ChatMessage.builder()
                    .content(request.getContent())
                    .sender(currentUser)
                    .createdAt(LocalDateTime.now())
                    .isEveryoneMention(request.isEveryoneMention())
                    .mentions(new HashSet<>())
                    .build();

            // Save the initial message
            final ChatMessage savedMessage = chatMessageRepository.save(message);

            // Create mentions and notifications if needed
            if (!mentionedUsers.isEmpty()) {
                Set<ChatMention> mentions = mentionedUsers.stream()
                        .map(user -> ChatMention.builder()
                                .message(savedMessage)
                                .mentionedUser(user)
                                .build())
                        .collect(Collectors.toSet());
                savedMessage.setMentions(mentions);

                // Save again with the mention details
                ChatMessage finalMessage = chatMessageRepository.save(savedMessage);
                createMentionNotifications(finalMessage, mentionedUsers);
                return mapToDTO(finalMessage);
            }

            return mapToDTO(savedMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating message: " + e.getMessage());
            System.err.println("Request details: " + request);
            throw new RuntimeException("Failed to create chat message", e);
        }
    }

    @Transactional
    public ChatMessageDTO updateMessage(Long messageId, ChatMessageRequest request) {
        User currentUser = userService.getCurrentUser();
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getSender().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only edit your own messages");
        }

        message.setContent(request.getContent());
        message.setUpdatedAt(LocalDateTime.now());
        
        return mapToDTO(chatMessageRepository.save(message));
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        User currentUser = userService.getCurrentUser();
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        boolean hasDeletePermission = roleService.roleHasPermission(
                currentUser.getRole(), Module.CHAT, PermissionType.DELETE);

        if (!message.getSender().getId().equals(currentUser.getId()) && !hasDeletePermission) {
            throw new AccessDeniedException("You can only delete your own messages or need delete permission");
        }

        chatMessageRepository.delete(message);
    }

    private ChatMessageDTO mapToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setSenderRole(message.getSender().getRole().getName());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        dto.setEveryoneMention(message.isEveryoneMention());

        Set<String> mentionedUsernames = message.getMentions().stream()
                .map(mention -> mention.getMentionedUser().getUsername())
                .collect(Collectors.toSet());
        dto.setMentionedUsernames(mentionedUsernames);

        return dto;
    }

    private void createMentionNotifications(ChatMessage message, Set<User> mentionedUsers) {
        String senderName = message.getSender().getUsername();

        for (User mentionedUser : mentionedUsers) {
            if (!mentionedUser.getId().equals(message.getSender().getId())) {
                notificationService.createNotification(
                        "Chat Mention",
                        String.format("%s mentioned you: %s", senderName, message.getContent()),
                        "CHAT",
                        "MENTION",
                        "LOW",
                        mentionedUser.getId(),
                        message.getId(),
                        "CHAT_MESSAGE"
                );
            }
        }
    }
}
