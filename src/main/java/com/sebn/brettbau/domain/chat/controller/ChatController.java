// src/main/java/com/sebn/brettbau/domain/chat/controller/ChatController.java
package com.sebn.brettbau.domain.chat.controller;

import com.sebn.brettbau.domain.chat.dto.ChatMessageDTO;
import com.sebn.brettbau.domain.chat.dto.ChatMessageRequest;
import com.sebn.brettbau.domain.chat.service.ChatService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://10.150.2.201:3000")
@Validated
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ChatMessageDTO>> getAllMessages() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read chat messages");
        }
        return ResponseEntity.ok(chatService.getAllMessages());
    }

    @PostMapping
    public ResponseEntity<ChatMessageDTO> createMessage(@Validated @RequestBody ChatMessageRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to send chat messages");
        }
        return ResponseEntity.ok(chatService.createMessage(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatMessageDTO> updateMessage(
            @PathVariable Long id,
            @Validated @RequestBody ChatMessageRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update chat messages");
        }
        return ResponseEntity.ok(chatService.updateMessage(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHAT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete chat messages");
        }
        chatService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}