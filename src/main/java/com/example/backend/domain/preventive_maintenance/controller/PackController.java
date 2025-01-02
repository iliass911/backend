// src/main/java/com/example/backend/domain/preventive_maintenance/controller/PackController.java

package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.PackDTO;
import com.example.backend.domain.preventive_maintenance.service.PackService;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController {

    private final PackService packService;

    @Autowired
    public PackController(PackService packService) {
        this.packService = packService;
    }

    // Get all packs
    @GetMapping
    public List<PackDTO> getAllPacks() {
        return packService.getAllPacks();
    }

    // Get packs by site ID
    @GetMapping("/site/{siteId}")
    public List<PackDTO> getPacksBySiteId(@PathVariable Long siteId) {
        return packService.getPacksBySiteId(siteId);
    }

    // Get pack by ID
    @GetMapping("/{id}")
    public ResponseEntity<PackDTO> getPackById(@PathVariable Long id) {
        try {
            PackDTO packDTO = packService.getPackById(id);
            return ResponseEntity.ok(packDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create new pack
    @PostMapping
    public ResponseEntity<PackDTO> createPack(@Valid @RequestBody PackDTO packDTO) {
        PackDTO createdPack = packService.createPack(packDTO);
        return ResponseEntity.ok(createdPack);
    }

    // Update existing pack
    @PutMapping("/{id}")
    public ResponseEntity<PackDTO> updatePack(@PathVariable Long id, @Valid @RequestBody PackDTO packDTO) {
        try {
            PackDTO updatedPack = packService.updatePack(id, packDTO);
            return ResponseEntity.ok(updatedPack);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete pack
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable Long id) {
        try {
            packService.deletePack(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
