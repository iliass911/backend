package com.sebn.brettbau.domain.inventory.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class InventoryItemDTO {
    private Long id;

    @NotBlank
    private String refCode;

    @NotBlank
    private String site;

    private String type;

    @NotNull
    private Integer quantity;

    private String place;
    private String unit;

    @NotNull
    private Double price;

    private String sezamNumber;  // New field sezamNumber

    // totalPrice will be computed by the mapper or after converting from entity
    private Double totalPrice;
}
