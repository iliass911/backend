package com.sebn.brettbau.domain.inventory.controller;

import com.sebn.brettbau.domain.inventory.dto.InventoryMovementDTO;
import com.sebn.brettbau.domain.inventory.service.InventoryMovementService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/inventory/movements")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class InventoryMovementController {

    private final InventoryMovementService movementService;
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<InventoryMovementDTO>> getAllMovements(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to view inventory movement history");
        }
        
        return ResponseEntity.ok(movementService.getAllMovements(pageable));
    }
    
    @GetMapping("/item/{itemId}")
    public ResponseEntity<Page<InventoryMovementDTO>> getMovementsForItem(
            @PathVariable Long itemId, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to view inventory movement history");
        }
        
        return ResponseEntity.ok(movementService.getMovementsForItem(itemId, pageable));
    }
    
    @GetMapping("/site/{site}")
    public ResponseEntity<Page<InventoryMovementDTO>> getMovementsBySite(
            @PathVariable String site, Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to view inventory movement history");
        }
        
        return ResponseEntity.ok(movementService.getMovementsBySite(site, pageable));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<Page<InventoryMovementDTO>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to view inventory movement history");
        }
        
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        
        return ResponseEntity.ok(movementService.getMovementsByDateRange(start, end, pageable));
    }
}