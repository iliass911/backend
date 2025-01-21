package com.sebn.brettbau.domain.inventory.service;

import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.inventory.dto.InventoryItemDTO;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import com.sebn.brettbau.domain.inventory.mapper.InventoryItemMapper;
import com.sebn.brettbau.domain.inventory.repository.InventoryItemRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository repository;
    private final AuditLogService auditLogService;
    private final UserService userService;

    public InventoryItemDTO createItem(InventoryItemDTO dto) {
        InventoryItem entity = InventoryItemMapper.toEntity(dto);
        InventoryItem saved = repository.save(entity);

        User currentUser = userService.getCurrentUser();
        auditLogService.logEvent(
            currentUser,
            "INVENTORY_CREATED",
            String.format("Inventory item created: %s, Quantity: %d, Site: %s",
                saved.getRefCode(), saved.getQuantity(), saved.getSite()),
            "INVENTORY",
            saved.getId()
        );

        return InventoryItemMapper.toDTO(saved);
    }

    public List<InventoryItemDTO> getAllItems() {
        return repository.findAll().stream()
                .map(InventoryItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryItemDTO getItemById(Long id) {
        return repository.findById(id)
                .map(InventoryItemMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
    }

    public InventoryItemDTO updateItem(Long id, InventoryItemDTO dto) {
        InventoryItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        String oldDetails = String.format("RefCode: %s, Quantity: %d, Site: %s",
            entity.getRefCode(), entity.getQuantity(), entity.getSite());

        // Update all relevant fields
        entity.setRefCode(dto.getRefCode());
        entity.setSite(dto.getSite());
        entity.setType(dto.getType());
        entity.setQuantity(dto.getQuantity());
        entity.setPlace(dto.getPlace());
        entity.setUnit(dto.getUnit());
        entity.setPrice(dto.getPrice());

        InventoryItem updated = repository.save(entity);

        User currentUser = userService.getCurrentUser();
        auditLogService.logEvent(
            currentUser,
            "INVENTORY_UPDATED",
            String.format("Inventory item updated from [%s] to [RefCode: %s, Quantity: %d, Site: %s]",
                oldDetails, updated.getRefCode(), updated.getQuantity(), updated.getSite()),
            "INVENTORY",
            updated.getId()
        );

        return InventoryItemMapper.toDTO(updated);
    }

    public void deleteItem(Long id) {
        InventoryItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        repository.deleteById(id);

        User currentUser = userService.getCurrentUser();
        auditLogService.logEvent(
            currentUser,
            "INVENTORY_DELETED",
            String.format("Inventory item deleted: %s, Quantity: %d, Site: %s",
                item.getRefCode(), item.getQuantity(), item.getSite()),
            "INVENTORY",
            id
        );
    }

    /**
     * Update only the quantity of an existing inventory item.
     * Logs an audit event indicating the quantity change.
     */
    @Transactional
    public InventoryItemDTO updateQuantityOnly(Long id, Integer quantity) {
        InventoryItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        // Record old quantity
        String oldDetails = String.format("Quantity: %d", entity.getQuantity());

        // Update only the quantity
        entity.setQuantity(quantity);

        InventoryItem updated = repository.save(entity);

        // Log the event
        User currentUser = userService.getCurrentUser();
        auditLogService.logEvent(
            currentUser,
            "INVENTORY_QUANTITY_UPDATED",
            String.format("Inventory quantity updated from [%s] to [Quantity: %d]",
                oldDetails, updated.getQuantity()),
            "INVENTORY",
            updated.getId()
        );

        return InventoryItemMapper.toDTO(updated);
    }
}
