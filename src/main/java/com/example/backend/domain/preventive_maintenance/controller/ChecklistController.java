// ChecklistController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController; 
import com.example.backend.domain.preventive_maintenance.dto.ChecklistDTO;
import com.example.backend.domain.preventive_maintenance.dto.ProgressResponse;
import com.example.backend.domain.preventive_maintenance.service.ChecklistService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController extends BaseController {
    private final ChecklistService checklistService;

    public ChecklistController(PermissionChecker permissionChecker, ChecklistService checklistService) {
        super(permissionChecker);
        this.checklistService = checklistService;
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists() {
        checkPermission("CHECKLIST", "VIEW");
        return ResponseEntity.ok(checklistService.getAllChecklists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklist(@PathVariable Long id) {
        checkPermission("CHECKLIST", "VIEW");
        return ResponseEntity.ok(checklistService.getChecklistById(id));
    }

    @PostMapping
    public ResponseEntity<ChecklistDTO> createChecklist(@RequestBody ChecklistDTO dto) {
        checkPermission("CHECKLIST", "CREATE");
        return ResponseEntity.ok(checklistService.createChecklist(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistDTO> updateChecklist(@PathVariable Long id, @RequestBody ChecklistDTO dto) {
        checkPermission("CHECKLIST", "UPDATE"); 
        return ResponseEntity.ok(checklistService.updateChecklist(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long id) {
        checkPermission("CHECKLIST", "DELETE");
        checklistService.deleteChecklist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ProgressResponse> getUserProgress(@PathVariable Long userId) {
        checkPermission("CHECKLIST", "VIEW");
        ProgressResponse progress = checklistService.getUserProgress(userId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        }
        return ResponseEntity.notFound().build();
    }
}