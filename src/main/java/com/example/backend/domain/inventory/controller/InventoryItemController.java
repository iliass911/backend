// InventoryItemController.java
package com.example.backend.domain.inventory.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.inventory.dto.InventoryItemDTO;
import com.example.backend.domain.inventory.service.InventoryItemService;
import com.example.backend.domain.role.service.PermissionChecker;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryItemController extends BaseController {
    private final InventoryItemService service;
    
    public InventoryItemController(PermissionChecker permissionChecker, InventoryItemService service) {
        super(permissionChecker);
        this.service = service;
    }

    @PostMapping
    public InventoryItemDTO create(@RequestBody @Validated InventoryItemDTO dto) {
        checkPermission("INVENTORY", "CREATE");
        return service.createItem(dto);
    }

    @GetMapping  
    public List<InventoryItemDTO> getAll() {
        checkPermission("INVENTORY", "VIEW");
        return service.getAllItems();
    }

    @GetMapping("/{id}")
    public InventoryItemDTO getById(@PathVariable Long id) {
        checkPermission("INVENTORY", "VIEW");
        return service.getItemById(id);
    }

    @PutMapping("/{id}")
    public InventoryItemDTO update(@PathVariable Long id,
                                   @RequestBody @Validated InventoryItemDTO dto) {
       checkPermission("INVENTORY", "UPDATE");
       if (dto.getQuantity() == null) {
           throw new ResourceNotFoundException("Quantity must be provided for updates.");
       }
       return service.updateQuantityOnly(id, dto.getQuantity());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        checkPermission("INVENTORY", "DELETE");
        service.deleteItem(id);
    }
}