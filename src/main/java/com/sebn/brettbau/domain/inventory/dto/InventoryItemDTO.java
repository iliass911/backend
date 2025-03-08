package com.sebn.brettbau.domain.inventory.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Data
public class InventoryItemDTO {
    private Long id;

    @NotBlank
    private String refCode;

    @NotBlank
    private String site;

    private String type;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Integer minQuantity;

    @NotNull
    @Min(0)
    private Integer maxQuantity;

    private String place;
    private String unit;

    @NotNull
    private Double price;

    private String sezamNumber;
    private Double totalPrice;
    
    // New fields for stock status
    private boolean lowStock;
    private boolean overStock;
    
    // Transient field for update reason
    private String reason;
}