package com.example.backend.domain.inventory.controller;

import com.example.backend.domain.inventory.dto.InventoryItemDTO;
import com.example.backend.domain.inventory.service.InventoryItemService;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryItemController {

    private final InventoryItemService service;

    public InventoryItemController(InventoryItemService service) {
        this.service = service;
    }

    /**
     * Create a new inventory item.
     * Only an Admin can create.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public InventoryItemDTO create(@RequestBody @Validated InventoryItemDTO dto) {
        return service.createItem(dto);
    }

    /**
     * Retrieve all inventory items.
     * Both Admin and User can view.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<InventoryItemDTO> getAll() {
        return service.getAllItems();
    }

    /**
     * Retrieve a single inventory item by ID.
     * Both Admin and User can view.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public InventoryItemDTO getById(@PathVariable Long id) {
        return service.getItemById(id);
    }

    /**
     * Update an inventory item.
     * Both Admin and User can call this endpoint.
     * Non-admin users are restricted to only updating quantity.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public InventoryItemDTO update(@PathVariable Long id,
                                   @RequestBody @Validated InventoryItemDTO dto) {

        // Determine if the current user has an Admin role
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // If not an Admin, only allow updating the quantity
        if (!isAdmin) {
            if (dto.getQuantity() == null) {
                throw new ResourceNotFoundException("Quantity must be provided for non-admin updates.");
            }
            return service.updateQuantityOnly(id, dto.getQuantity());
        }

        // If Admin, allow a full update
        return service.updateItem(id, dto);
    }

    /**
     * Delete an inventory item by ID.
     * Only Admin can delete.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.deleteItem(id);
    }
}
