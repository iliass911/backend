package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.example.backend.domain.preventive_maintenance.service.BoardFamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-families")
public class BoardFamilyController {
    private final BoardFamilyService boardFamilyService;

    public BoardFamilyController(BoardFamilyService boardFamilyService) {
        this.boardFamilyService = boardFamilyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateFamilies() {
        try {
            int familiesCreated = boardFamilyService.generateFamiliesForExistingBoards();
            return ResponseEntity.ok("Successfully created " + familiesCreated + " board families");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating families: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BoardFamilyDTO>> getAllFamilies() {
        return ResponseEntity.ok(boardFamilyService.getAllFamilies());
    }
}
