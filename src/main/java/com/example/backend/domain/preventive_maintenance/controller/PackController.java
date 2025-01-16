// PackController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.PackDTO;
import com.example.backend.domain.preventive_maintenance.service.PackService;
import com.example.backend.domain.role.service.PermissionChecker;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController extends BaseController {

    private final PackService packService;

    public PackController(PermissionChecker permissionChecker, PackService packService) {
        super(permissionChecker);
        this.packService = packService;
    }

    @GetMapping
    public List<PackDTO> getAllPacks() {
        checkPermission("PACK", "VIEW"); 
        return packService.getAllPacks();
    }

    @GetMapping("/site/{siteId}")
    public List<PackDTO> getPacksBySiteId(@PathVariable Long siteId) {
        checkPermission("PACK", "VIEW");
        return packService.getPacksBySiteId(siteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackDTO> getPackById(@PathVariable Long id) {
        checkPermission("PACK", "VIEW");
        try {
            PackDTO packDTO = packService.getPackById(id);
            return ResponseEntity.ok(packDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PackDTO> createPack(@Valid @RequestBody PackDTO packDTO) {
        checkPermission("PACK", "CREATE");
        PackDTO createdPack = packService.createPack(packDTO);
        return ResponseEntity.ok(createdPack);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackDTO> updatePack(@PathVariable Long id, @Valid @RequestBody PackDTO packDTO) {
        checkPermission("PACK", "UPDATE");
        try {
            PackDTO updatedPack = packService.updatePack(id, packDTO);
            return ResponseEntity.ok(updatedPack);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable Long id) {
        checkPermission("PACK", "DELETE");
        try {
            packService.deletePack(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}