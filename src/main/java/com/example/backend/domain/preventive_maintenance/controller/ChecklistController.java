// src/main/java/com/example/backend/domain/preventive_maintenance/controller/ChecklistController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.ChecklistDTO;
import com.example.backend.domain.preventive_maintenance.dto.ProgressResponse;
import com.example.backend.domain.preventive_maintenance.service.ChecklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController {
    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists() {
        return ResponseEntity.ok(checklistService.getAllChecklists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklist(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getChecklistById(id));
    }

    @PostMapping
    public ResponseEntity<ChecklistDTO> createChecklist(@RequestBody ChecklistDTO dto) {
        return ResponseEntity.ok(checklistService.createChecklist(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistDTO> updateChecklist(@PathVariable Long id, @RequestBody ChecklistDTO dto) {
        return ResponseEntity.ok(checklistService.updateChecklist(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long id) {
        checklistService.deleteChecklist(id);
        return ResponseEntity.noContent().build();
    }

    // Add Progress Endpoint
    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ProgressResponse> getUserProgress(@PathVariable Long userId) {
        ProgressResponse progress = checklistService.getUserProgress(userId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        }
        return ResponseEntity.notFound().build();
    }
}
