package com.sebn.brettbau.domain.inventory.entity;

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
    private String refCode; 

    @Column(nullable = false)
    private String site;     

    private String type;    
    private Integer quantity; 
    private String place;    
    private String unit;    
    private Double price;   

    @Column
    private String sezamNumber; 
    
    @Transient
    public Double getTotalPrice() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return 0.0;
    }
}
