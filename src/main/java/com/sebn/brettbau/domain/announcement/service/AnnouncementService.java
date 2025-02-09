package com.sebn.brettbau.domain.announcement.service;

import com.sebn.brettbau.domain.announcement.dto.AnnouncementDTO;
import com.sebn.brettbau.domain.announcement.dto.AnnouncementRequest;
import com.sebn.brettbau.domain.announcement.entity.Announcement;
import com.sebn.brettbau.domain.announcement.repository.AnnouncementRepository;
import com.sebn.brettbau.domain.notification.service.NotificationService;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final RoleService roleService;

    @Transactional(readOnly = true)
    public List<AnnouncementDTO> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AnnouncementDTO getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
    }

    @Transactional
    public AnnouncementDTO createAnnouncement(AnnouncementRequest request) {
        User currentUser = userService.getCurrentUser();

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .creator(currentUser)
                .isAnonymous(request.isAnonymous())
                .targetModules(request.getTargetModules())
                .createdAt(LocalDateTime.now())
                .build();

        announcement = announcementRepository.save(announcement);
        createAnnouncementNotifications(announcement);
        return mapToDTO(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement not found with id: " + id));
        announcementRepository.delete(announcement);
    }

    private AnnouncementDTO mapToDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setMessage(announcement.getMessage());
        dto.setCreatorUsername(announcement.isAnonymous() ? "Anonymous" : announcement.getCreator().getUsername());
        dto.setAnonymous(announcement.isAnonymous());
        dto.setTargetModules(announcement.getTargetModules());
        dto.setCreatedAt(announcement.getCreatedAt());
        return dto;
    }

    private void createAnnouncementNotifications(Announcement announcement) {
        List<User> allUsers = userService.getAllUsers();
        
        for (User user : allUsers) {
            boolean hasAccess = announcement.getTargetModules().stream()
                    .anyMatch(module -> roleService.roleHasPermission(user.getRole(), module, PermissionType.READ));
            
            if (hasAccess) {
                String creatorInfo = announcement.isAnonymous() ? "Anonymous" : announcement.getCreator().getUsername();
                String notificationTitle = "📢 " + announcement.getTitle();
                String notificationMessage = announcement.getMessage() + "\n\nFrom: " + creatorInfo;
                
                notificationService.createNotification(
                    notificationTitle,
                    notificationMessage,
                    "ANNOUNCEMENT",
                    "ANNOUNCEMENT",
                    "MEDIUM",
                    user.getId(),
                    announcement.getId(),
                    "ANNOUNCEMENT"
                );
            }
        }
    }
}