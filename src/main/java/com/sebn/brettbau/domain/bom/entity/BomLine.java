package com.sebn.brettbau.domain.bom.entity;

import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "bom_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the BOM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    private Bom bom;

    // If referencing an InventoryItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    // Category (holder 2d, holder 3d, em, etc.)
    @Column(nullable = false)
    private String category;

    // Name/description of the component
    @Column(nullable = false)
    private String componentName;

    // Unit price retrieved from the Inventory item or set manually
    private Double unitPrice;

    // Chosen quantity for this board
    private Integer quantity;

    // Computed cost = unitPrice * quantity
    private Double lineCost;

    // One-to-Many relationship with BomLineUnit
    @OneToMany(mappedBy = "bomLine", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("unitIndex")
    private List<BomLineUnit> units = new ArrayList<>();

    // Helper method to calculate lineCost before persisting or updating
    @PrePersist
    @PreUpdate
    private void calculateLineCost() {
        if (unitPrice != null && quantity != null) {
            this.lineCost = unitPrice * quantity;
        } else {
            this.lineCost = 0.0;
        }
    }

    // Helper method to manage unit names
    public void setUnitNames(List<String> unitNames) {
        units.clear();
        if (unitNames != null) {
            for (int i = 0; i < unitNames.size(); i++) {
                String unitName = unitNames.get(i);
                if (unitName != null && !unitName.trim().isEmpty()) {
                    BomLineUnit unit = BomLineUnit.builder()
                            .bomLine(this)
                            .unitIndex(i)
                            .unitName(unitName)
                            .build();
                    units.add(unit);
                }
            }
        }
    }

    // Helper method to retrieve unit names
    public List<String> getUnitNames() {
        return units.stream()
                .sorted(Comparator.comparing(BomLineUnit::getUnitIndex))
                .map(BomLineUnit::getUnitName)
                .collect(Collectors.toList());
    }
}

