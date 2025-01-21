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
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;

    public InventoryItemController(InventoryItemService service) {
        this.service = service;
    }

    /**
     * Create a new inventory item.
     * Requires CREATE permission on INVENTORY module
     */
    @PostMapping
    public ResponseEntity<InventoryItemDTO> create(@RequestBody @Validated InventoryItemDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE inventory items.");
        }
        return ResponseEntity.ok(service.createItem(dto));
    }

    /**
     * Retrieve all inventory items.
     * Requires READ permission on INVENTORY module
     */
    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ inventory items.");
        }
        return ResponseEntity.ok(service.getAllItems());
    }

    /**
     * Retrieve a single inventory item by ID.
     * Requires READ permission on INVENTORY module
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ inventory items.");
        }
        return ResponseEntity.ok(service.getItemById(id));
    }

    /**
     * Update an inventory item.
     * Requires UPDATE permission on INVENTORY module
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated InventoryItemDTO dto
    ) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE inventory items.");
        }
        return ResponseEntity.ok(service.updateItem(id, dto));
    }

    /**
     * Delete an inventory item by ID.
     * Requires DELETE permission on INVENTORY module
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.INVENTORY, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE inventory items.");
        }
        service.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}