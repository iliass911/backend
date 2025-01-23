package com.sebn.brettbau.domain.inventory.controller;

import com.sebn.brettbau.domain.inventory.dto.InventoryItemDTO;
import com.sebn.brettbau.domain.inventory.service.InventoryItemService;
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
     */
    @PostMapping
    public InventoryItemDTO create(@RequestBody @Validated InventoryItemDTO dto) {
        return service.createItem(dto);
    }

    /**
     * Retrieve all inventory items.
     */
    @GetMapping
    public List<InventoryItemDTO> getAll() {
        return service.getAllItems();
    }

    /**
     * Retrieve a single inventory item by ID.
     */
    @GetMapping("/{id}")
    public InventoryItemDTO getById(@PathVariable Long id) {
        return service.getItemById(id);
    }

    /**
     * Update an inventory item.
     * All users can perform a full update.
     */
    @PutMapping("/{id}")
    public InventoryItemDTO update(@PathVariable Long id,
                                   @RequestBody @Validated InventoryItemDTO dto) {
        return service.updateItem(id, dto);
    }

    /**
     * Delete an inventory item by ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteItem(id);
    }
}
