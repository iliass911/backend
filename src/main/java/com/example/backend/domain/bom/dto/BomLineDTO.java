package com.example.backend.domain.bom.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BomLineDTO {
    private Long id;
    private Long bomId;
    private Long inventoryItemId;
    private String category;
    private String componentName;
    private Double unitPrice;
    private Integer quantity;
    private Double lineCost;
    private List<String> unitNames;
}