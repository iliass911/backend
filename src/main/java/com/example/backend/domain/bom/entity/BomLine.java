package com.example.backend.domain.bom.entity;

import com.example.backend.domain.inventory.entity.InventoryItem;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bom_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"bom", "inventoryItem", "units"})
public class BomLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bom_id", nullable = false)
    private Bom bom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String componentName;

    private Double unitPrice;

    private Integer quantity;

    private Double lineCost;

    @OneToMany(
        mappedBy = "bomLine",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @OrderBy("unitIndex ASC")
    private List<BomLineUnit> units = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void calculateLineCost() {
        if (unitPrice != null && quantity != null) {
            this.lineCost = unitPrice * quantity;
        } else {
            this.lineCost = 0.0;
        }
    }

    public void setUnits(List<BomLineUnit> units) {
        this.units.clear();
        if (units != null) {
            units.forEach(unit -> {
                unit.setBomLine(this);
                this.units.add(unit);
            });
        }
    }

    public void addUnit(BomLineUnit unit) {
        units.add(unit);
        unit.setBomLine(this);
    }

    public void removeUnit(BomLineUnit unit) {
        units.remove(unit);
        unit.setBomLine(null);
    }
}