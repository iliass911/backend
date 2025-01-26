// src/main/java/com/sebn/brettbau/domain/inventory/controller/InventoryItemController.java

package com.sebn.brettbau.domain.inventory.controller;

import com.sebn.brettbau.domain.inventory.dto.InventoryItemDTO;
import com.sebn.brettbau.domain.inventory.service.InventoryItemService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class InventoryItemController {

    private final InventoryItemService service;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public InventoryItemController(
            InventoryItemService service,
            RoleService roleService,
            UserService userService) {
        this.service = service;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Create a new inventory item.
     */
    @PostMapping
    public ResponseEntity<InventoryItemDTO> create(@RequestBody @Validated InventoryItemDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create inventory items");
        }
        InventoryItemDTO createdItem = service.createItem(dto);
        return ResponseEntity.ok(createdItem);
    }

    /**
     * Retrieve all inventory items.
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read inventory items");
        }
        List<InventoryItemDTO> items = service.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Retrieve a single inventory item by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read inventory items");
        }
        InventoryItemDTO item = service.getItemById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * Update an inventory item.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated InventoryItemDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update inventory items");
        }
        InventoryItemDTO updatedItem = service.updateItem(id, dto);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Delete an inventory item by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete inventory items");
        }
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
