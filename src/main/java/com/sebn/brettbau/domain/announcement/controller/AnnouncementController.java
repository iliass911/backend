package com.sebn.brettbau.domain.announcement.controller;

import com.sebn.brettbau.domain.announcement.dto.AnnouncementDTO;
import com.sebn.brettbau.domain.announcement.dto.AnnouncementRequest;
import com.sebn.brettbau.domain.announcement.service.AnnouncementService;
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
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://10.150.2.201:3000")
@Validated
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final RoleService roleService;
    private final UserService userService;

    public AnnouncementController(
            AnnouncementService announcementService,
            RoleService roleService,
            UserService userService) {
        this.announcementService = announcementService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ANNOUNCEMENT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read announcements");
        }
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ANNOUNCEMENT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read announcements");
        }
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(
            @RequestBody @Validated AnnouncementRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ANNOUNCEMENT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create announcements");
        }
        return ResponseEntity.ok(announcementService.createAnnouncement(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.ANNOUNCEMENT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete announcements");
        }
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}