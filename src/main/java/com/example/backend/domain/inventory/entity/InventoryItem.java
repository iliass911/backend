package com.example.backend.domain.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refCode;  // REF

    @Column(nullable = false)
    private String site;     // SITE

    private String type;     // Type
    private Integer quantity; // QUANTITY
    private String place;    // PLACE
    private String unit;     // UNIT
    private Double price;    // PRICE

    @Column
    private String sezamNumber;  // New field sezamNumber

    // TOTAL_PRICE is computed, not stored:
    @Transient
    public Double getTotalPrice() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0.0;
    }
}
