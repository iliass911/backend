package com.sebn.brettbau.domain.inventory.service;

import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.inventory.dto.InventoryItemDTO;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import com.sebn.brettbau.domain.inventory.mapper.InventoryItemMapper;
import com.sebn.brettbau.domain.inventory.repository.InventoryItemRepository;
import com.sebn.brettbau.domain.notification.service.NotificationService;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryItemService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemService.class);

    private final InventoryItemRepository repository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final UserService userService;

    @Transactional
    public InventoryItemDTO createItem(InventoryItemDTO dto) {
        try {
            validateQuantityLevels(dto);
            InventoryItem entity = InventoryItemMapper.toEntity(dto);
            InventoryItem saved = repository.save(entity);
            
            checkStockLevels(saved);

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
        } catch (Exception e) {
            logger.error("Error creating inventory item", e);
            throw new RuntimeException("Failed to create inventory item: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryItemDTO> getAllItems() {
        return repository.findAll().stream()
                .map(InventoryItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventoryItemDTO getItemById(Long id) {
        return repository.findById(id)
                .map(InventoryItemMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
    }

    @Transactional
    public InventoryItemDTO updateItem(Long id, InventoryItemDTO dto) {
        try {
            validateQuantityLevels(dto);
            
            InventoryItem entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

            String oldDetails = String.format("RefCode: %s, Quantity: %d, Site: %s",
                entity.getRefCode(), entity.getQuantity(), entity.getSite());

            // Update fields
            entity.setRefCode(dto.getRefCode());
            entity.setSite(dto.getSite());
            entity.setType(dto.getType());
            entity.setQuantity(dto.getQuantity());
            entity.setMinQuantity(dto.getMinQuantity());
            entity.setMaxQuantity(dto.getMaxQuantity());
            entity.setPlace(dto.getPlace());
            entity.setUnit(dto.getUnit());
            entity.setPrice(dto.getPrice());
            entity.setSezamNumber(dto.getSezamNumber());

            InventoryItem updated = repository.save(entity);
            
            checkStockLevels(updated);

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
        } catch (Exception e) {
            logger.error("Error updating inventory item", e);
            throw new RuntimeException("Failed to update inventory item: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteItem(Long id) {
        try {
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
        } catch (Exception e) {
            logger.error("Error deleting inventory item", e);
            throw new RuntimeException("Failed to delete inventory item: " + e.getMessage());
        }
    }

    @Transactional
    public InventoryItemDTO updateQuantityOnly(Long id, Integer quantity) {
        try {
            InventoryItem entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

            String oldDetails = String.format("Quantity: %d", entity.getQuantity());
            entity.setQuantity(quantity);
            InventoryItem updated = repository.save(entity);
            
            checkStockLevels(updated);

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
        } catch (Exception e) {
            logger.error("Error updating inventory item quantity", e);
            throw new RuntimeException("Failed to update inventory item quantity: " + e.getMessage());
        }
    }

    /**
     * Validates the quantity levels from the provided DTO.
     * <p>
     * This method first ensures that all quantity values are non-negative.
     * Then, it logs warnings if:
     * <ul>
     *   <li>The minimum quantity is not less than the maximum quantity.</li>
     *   <li>The current quantity is below the minimum level.</li>
     *   <li>The current quantity exceeds the maximum level.</li>
     * </ul>
     * </p>
     *
     * @param dto the InventoryItemDTO containing quantity details.
     * @throws IllegalArgumentException if any quantity value is negative.
     */
    private void validateQuantityLevels(InventoryItemDTO dto) {
        // Only validate that quantities are not negative
        if (dto.getMinQuantity() < 0 || dto.getMaxQuantity() < 0 || dto.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity values cannot be negative");
        }
        
        // Log warnings but don't block the operation
        if (dto.getMinQuantity() >= dto.getMaxQuantity()) {
            logger.warn("Warning: Minimum quantity is not less than maximum quantity. Min: {}, Max: {}",
                        dto.getMinQuantity(), dto.getMaxQuantity());
        }
        
        // Log warnings for quantity outside bounds
        if (dto.getQuantity() < dto.getMinQuantity()) {
            logger.warn("Warning: Current quantity is below minimum. Current: {}, Min: {}",
                        dto.getQuantity(), dto.getMinQuantity());
        }
        
        if (dto.getQuantity() > dto.getMaxQuantity()) {
            logger.warn("Warning: Current quantity exceeds maximum. Current: {}, Max: {}",
                        dto.getQuantity(), dto.getMaxQuantity());
        }
    }

    /**
     * Checks the stock levels of the given inventory item and creates notifications
     * for low stock or overstock situations. Each notification call is wrapped in its own
     * try–catch block so that any failure in creating a notification does not block the operation.
     *
     * @param item the inventory item to check.
     */
    private void checkStockLevels(InventoryItem item) {
        try {
            User admin = userService.getCurrentUser();
            
            // Create notifications but don't block the save operation
            if (item.isLowStock()) {
                try {
                    notificationService.createNotification(
                        "Low Stock Alert: " + item.getRefCode(),
                        String.format("Item %s is running low. Current quantity (%d) is at or below minimum level (%d).",
                            item.getRefCode(), item.getQuantity(), item.getMinQuantity()),
                        "INVENTORY",
                        "WARNING",
                        "HIGH",
                        admin.getId(),
                        item.getId(),
                        "INVENTORY_ITEM"
                    );
                } catch (Exception e) {
                    // Log but do not rethrow the exception
                    logger.error("Failed to create low stock notification", e);
                }
            }
            
            if (item.isOverStock()) {
                try {
                    notificationService.createNotification(
                        "Over Stock Alert: " + item.getRefCode(),
                        String.format("Item %s has exceeded maximum level. Current quantity (%d) is at or above maximum level (%d).",
                            item.getRefCode(), item.getQuantity(), item.getMaxQuantity()),
                        "INVENTORY",
                        "WARNING",
                        "MEDIUM",
                        admin.getId(),
                        item.getId(),
                        "INVENTORY_ITEM"
                    );
                } catch (Exception e) {
                    // Log but do not rethrow the exception
                    logger.error("Failed to create over stock notification", e);
                }
            }
        } catch (Exception e) {
            // Log any unexpected error during the stock level check
            logger.error("Error checking stock levels", e);
        }
    }
}
