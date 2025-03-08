package com.sebn.brettbau.domain.inventory.mapper;

import com.sebn.brettbau.domain.inventory.dto.InventoryMovementDTO;
import com.sebn.brettbau.domain.inventory.entity.InventoryMovement;

public class InventoryMovementMapper {

    public static InventoryMovementDTO toDTO(InventoryMovement entity) {
        if (entity == null) return null;
        
        InventoryMovementDTO dto = new InventoryMovementDTO();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setItemRefCode(entity.getItem().getRefCode());
        dto.setItemSite(entity.getItem().getSite());
        dto.setItemType(entity.getItem().getType());
        dto.setItemPlace(entity.getItem().getPlace());
        dto.setUserId(entity.getUser().getId());
        dto.setUsername(entity.getUser().getUsername());
        dto.setTimestamp(entity.getTimestamp());
        dto.setPreviousQuantity(entity.getPreviousQuantity());
        dto.setNewQuantity(entity.getNewQuantity());
        dto.setQuantityChanged(entity.getQuantityChanged());
        dto.setMovementType(entity.getMovementType());
        dto.setReason(entity.getReason());
        
        return dto;
    }
}