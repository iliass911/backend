package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.PackDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.PackService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController {

    private final PackService packService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    public PackController(PackService packService) {
        this.packService = packService;
    }

    @GetMapping
    public List<PackDTO> getAllPacks() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view packs.");
        }
        return packService.getAllPacks();
    }

    @GetMapping("/site/{siteId}")
    public List<PackDTO> getPacksBySiteId(@PathVariable Long siteId) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view packs.");
        }
        return packService.getPacksBySiteId(siteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackDTO> getPackById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view packs.");
        }
        try {
            PackDTO packDTO = packService.getPackById(id);
            return ResponseEntity.ok(packDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PackDTO> createPack(@Valid @RequestBody PackDTO packDTO) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create packs.");
        }
        PackDTO createdPack = packService.createPack(packDTO);
        return ResponseEntity.ok(createdPack);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackDTO> updatePack(@PathVariable Long id, @Valid @RequestBody PackDTO packDTO) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update packs.");
        }
        try {
            PackDTO updatedPack = packService.updatePack(id, packDTO);
            return ResponseEntity.ok(updatedPack);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete packs.");
        }
        try {
            packService.deletePack(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}