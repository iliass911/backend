package com.sebn.brettbau.domain.inventory.mapper;

import com.sebn.brettbau.domain.inventory.dto.InventoryItemDTO;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;

public class InventoryItemMapper {

    public static InventoryItemDTO toDTO(InventoryItem entity) {
        if (entity == null) return null;
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setId(entity.getId());
        dto.setRefCode(entity.getRefCode());
        dto.setSite(entity.getSite());
        dto.setType(entity.getType());
        dto.setQuantity(entity.getQuantity());
        dto.setPlace(entity.getPlace());
        dto.setUnit(entity.getUnit());
        dto.setPrice(entity.getPrice());
        dto.setSezamNumber(entity.getSezamNumber());  // Added mapping for sezamNumber
        // Compute totalPrice from entity method
        dto.setTotalPrice(entity.getTotalPrice());
        return dto;
    }

    public static InventoryItem toEntity(InventoryItemDTO dto) {
        if (dto == null) return null;
        return InventoryItem.builder()
                .id(dto.getId())
                .refCode(dto.getRefCode())
                .site(dto.getSite())
                .type(dto.getType())
                .quantity(dto.getQuantity())
                .place(dto.getPlace())
                .unit(dto.getUnit())
                .price(dto.getPrice())
                .sezamNumber(dto.getSezamNumber())  // Added mapping for sezamNumber
                .build();
    }
}
