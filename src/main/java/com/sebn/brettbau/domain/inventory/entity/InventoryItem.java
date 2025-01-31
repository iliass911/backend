package com.sebn.brettbau.domain.inventory.entity;

import javax.persistence.*;
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
    private String refCode;

    @Column(nullable = false)
    private String site;

    private String type;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Integer minQuantity;
    
    @Column(nullable = false)
    private Integer maxQuantity;
    
    private String place;
    private String unit;
    private Double price;
    private String sezamNumber;

    @Transient
    public Double getTotalPrice() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0.0;
    }

    @Transient
    public boolean isLowStock() {
        return quantity != null && minQuantity != null && quantity <= minQuantity;
    }

    @Transient
    public boolean isOverStock() {
        return quantity != null && maxQuantity != null && quantity >= maxQuantity;
    }
}
