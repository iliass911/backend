package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ChecklistDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.ProgressResponse;
import com.sebn.brettbau.domain.preventive_maintenance.service.ChecklistService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController {
    private final ChecklistService checklistService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view checklists.");
        }
        return ResponseEntity.ok(checklistService.getAllChecklists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklist(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view checklists.");
        }
        return ResponseEntity.ok(checklistService.getChecklistById(id));
    }

    @PostMapping
    public ResponseEntity<ChecklistDTO> createChecklist(@RequestBody ChecklistDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create checklists.");
        }
        return ResponseEntity.ok(checklistService.createChecklist(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistDTO> updateChecklist(@PathVariable Long id, @RequestBody ChecklistDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update checklists.");
        }
        return ResponseEntity.ok(checklistService.updateChecklist(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete checklists.");
        }
        checklistService.deleteChecklist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ProgressResponse> getUserProgress(@PathVariable Long userId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.CHECKLIST, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view checklist progress.");
        }
        ProgressResponse progress = checklistService.getUserProgress(userId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        }
        return ResponseEntity.notFound().build();
    }
}