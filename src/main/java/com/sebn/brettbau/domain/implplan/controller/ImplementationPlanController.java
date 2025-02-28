package com.sebn.brettbau.domain.implplan.controller;

import com.sebn.brettbau.domain.implplan.dto.ImplementationPlanDTO;
import com.sebn.brettbau.domain.implplan.service.ImplementationPlanService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/impl-plan")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // or your allowed origins
public class ImplementationPlanController {

    private final ImplementationPlanService service;
    // If you have role/permission checks, inject RoleService & UserService here

    @GetMapping
    public ResponseEntity<List<ImplementationPlanDTO>> getAll() {
        // Example: check permission
        // if (!roleService.hasPermissionOrIndirectAccess(userService.getCurrentUser().getRole(), Module.CUSTOM, PermissionType.READ, null)) {
        //    throw new AccessDeniedException("No permission to read ImplementationPlan data.");
        // }
        List<ImplementationPlanDTO> all = service.getAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImplementationPlanDTO> getOne(@PathVariable Long id) {
        ImplementationPlanDTO dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ImplementationPlanDTO> create(@RequestBody ImplementationPlanDTO dto) {
        // Check permission: CREATE
        ImplementationPlanDTO created = service.createOrUpdate(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImplementationPlanDTO> update(@PathVariable Long id, @RequestBody ImplementationPlanDTO dto) {
        // Optionally ensure ID matches
        dto.setId(id);
        ImplementationPlanDTO updated = service.createOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Check permission: DELETE
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------------
    // EXCEL IMPORT ENDPOINT
    // -----------------------------
    @PostMapping("/import")
    public ResponseEntity<List<ImplementationPlanDTO>> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<ImplementationPlanDTO> imported = service.importFromExcel(file);
            return ResponseEntity.ok(imported);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
