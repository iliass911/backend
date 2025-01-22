package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardFamilyService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-families")
public class BoardFamilyController {
    private final BoardFamilyService boardFamilyService;
    private final UserService userService;
    private final RoleService roleService;

    public BoardFamilyController(
            BoardFamilyService boardFamilyService,
            UserService userService,
            RoleService roleService) {
        this.boardFamilyService = boardFamilyService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<BoardFamilyDTO>> getAllFamilies() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD_FAMILY, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view board families.");
        }
        return ResponseEntity.ok(boardFamilyService.getAllFamilies());
    }

    @PostMapping
    public ResponseEntity<BoardFamilyDTO> createFamily(@RequestBody String familyName) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD_FAMILY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create board families.");
        }
        return ResponseEntity.ok(boardFamilyService.createFamily(familyName));
    }

    @PostMapping("/{familyId}/boards/{boardId}")
    public ResponseEntity<BoardFamilyDTO> addBoardToFamily(
            @PathVariable Long familyId,
            @PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD_FAMILY, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update board families.");
        }
        return ResponseEntity.ok(boardFamilyService.addBoardToFamily(familyId, boardId));
    }

    @DeleteMapping("/{familyId}/boards/{boardId}")
    public ResponseEntity<BoardFamilyDTO> removeBoardFromFamily(
            @PathVariable Long familyId,
            @PathVariable Long boardId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD_FAMILY, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update board families.");
        }
        return ResponseEntity.ok(boardFamilyService.removeBoardFromFamily(familyId, boardId));
    }

    @DeleteMapping("/{familyId}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long familyId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD_FAMILY, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete board families.");
        }
        boardFamilyService.deleteFamily(familyId);
        return ResponseEntity.noContent().build();
    }
}