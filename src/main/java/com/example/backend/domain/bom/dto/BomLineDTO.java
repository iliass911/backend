package com.example.backend.domain.bom.dto;

import lombok.*;
import java.util.List; // Import List from java.util

/**
 * Data Transfer Object for BomLine entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomLineDTO {
    /**
     * Unique identifier for the BOM line.
     * Changed from Long to String to handle both temporary and permanent IDs.
     * Temporary IDs can start with a prefix like "temp-" before being persisted.
     */
    private String id;
    
    /**
     * Identifier of the associated BOM.
     */
    private Long bomId;
    
    /**
     * Identifier of the referenced InventoryItem.
     * Can be null if not referencing an InventoryItem.
     */
    private Long inventoryItemId;
    
    /**
     * Category of the component (e.g., "holder 2d", "holder 3d", "em", etc.).
     */
    private String category;
    
    /**
     * Name or description of the component.
     */
    private String componentName;
    
    /**
     * Unit price retrieved from the InventoryItem or set manually.
     */
    private Double unitPrice;
    
    /**
     * Chosen quantity for this BOM line.
     */
    private Integer quantity;
    
    /**
     * Computed cost = unitPrice * quantity.
     * This field is auto-calculated and should not be set manually.
     */
    private Double lineCost;
    
    /**
     * List of unit names associated with this BOM line.
     */
    private List<String> unitNames;

    /**
     * Helper method to check if the current ID is a temporary ID.
     *
     * @return true if the ID starts with "temp-", indicating a temporary ID; false otherwise.
     */
    public boolean isTemporary() {
        return id != null && id.startsWith("temp-");
    }
}
