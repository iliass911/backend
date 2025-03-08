package com.sebn.brettbau.domain.inventory.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InventoryMovementDTO {
    private Long id;
    private Long itemId;
    private String itemRefCode;
    private String itemSite;
    private String itemType;
    private String itemPlace;
    private Long userId;
    private String username;
    private LocalDateTime timestamp;
    private Integer previousQuantity;
    private Integer newQuantity;
    private Integer quantityChanged;
    private String movementType;
    private String reason;
}