// src/main/java/com/sebn/brettbau/domain/inventory/service/InventoryItemService.java
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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

    @Transactional
    public List<InventoryItemDTO> bulkUploadItems(MultipartFile file) {
        List<InventoryItemDTO> createdItems = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader("refCode", "site", "type", "quantity", "minQuantity", "maxQuantity", "place", "unit", "price", "sezamNumber")
                    .withSkipHeaderRecord(true)
                    .parse(reader);

            for (CSVRecord record : records) {
                InventoryItemDTO dto = new InventoryItemDTO();
                dto.setRefCode(record.get("refCode"));
                dto.setSite(record.get("site"));
                dto.setType(record.get("type"));
                dto.setQuantity(Integer.parseInt(record.get("quantity")));
                dto.setMinQuantity(Integer.parseInt(record.get("minQuantity")));
                dto.setMaxQuantity(Integer.parseInt(record.get("maxQuantity")));
                dto.setPlace(record.get("place"));
                dto.setUnit(record.get("unit"));
                dto.setPrice(Double.parseDouble(record.get("price")));
                dto.setSezamNumber(record.get("sezamNumber"));
                // Create each inventory item using the existing createItem method
                InventoryItemDTO created = createItem(dto);
                createdItems.add(created);
            }
        } catch (Exception e) {
            logger.error("Error processing bulk upload", e);
            throw new RuntimeException("Failed to process bulk upload: " + e.getMessage());
        }
        return createdItems;
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

    // --- Validation and Notification methods (unchanged) ---
    private void validateQuantityLevels(InventoryItemDTO dto) {
        if (dto.getMinQuantity() < 0 || dto.getMaxQuantity() < 0 || dto.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity values cannot be negative");
        }
        if (dto.getMinQuantity() >= dto.getMaxQuantity()) {
            logger.warn("Warning: Minimum quantity is not less than maximum quantity. Min: {}, Max: {}",
                        dto.getMinQuantity(), dto.getMaxQuantity());
        }
        if (dto.getQuantity() < dto.getMinQuantity()) {
            logger.warn("Warning: Current quantity is below minimum. Current: {}, Min: {}",
                        dto.getQuantity(), dto.getMinQuantity());
        }
        if (dto.getQuantity() > dto.getMaxQuantity()) {
            logger.warn("Warning: Current quantity exceeds maximum. Current: {}, Max: {}",
                        dto.getQuantity(), dto.getMaxQuantity());
        }
    }

    private void checkStockLevels(InventoryItem item) {
        try {
            User admin = userService.getCurrentUser();
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
                    logger.error("Failed to create over stock notification", e);
                }
            }
        } catch (Exception e) {
            logger.error("Error checking stock levels", e);
        }
    }
}
