// BoardFamilyController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.example.backend.domain.preventive_maintenance.service.BoardFamilyService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-families")
public class BoardFamilyController extends BaseController {
    private final BoardFamilyService boardFamilyService;

    public BoardFamilyController(PermissionChecker permissionChecker, BoardFamilyService boardFamilyService) {
        super(permissionChecker);
        this.boardFamilyService = boardFamilyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateFamilies() {
        checkPermission("BOARD_FAMILY", "CREATE");
        try {
            int familiesCreated = boardFamilyService.generateFamiliesForExistingBoards();
            return ResponseEntity.ok("Successfully created " + familiesCreated + " board families");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating families: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BoardFamilyDTO>> getAllFamilies() {
        checkPermission("BOARD_FAMILY", "VIEW");
        return ResponseEntity.ok(boardFamilyService.getAllFamilies());
    }
}