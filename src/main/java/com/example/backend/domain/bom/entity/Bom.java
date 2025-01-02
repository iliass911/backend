// src/main/java/com/example/backend/domain/bom/entity/Bom.java
package com.example.backend.domain.bom.entity;

import com.example.backend.domain.preventive_maintenance.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "boms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to a single Board
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // BOM lines referencing Inventory Items
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BomLine> bomLines = new HashSet<>();

    // Stores the total cost of the BOM (calculated from lines)
    private Double totalCost;

    /**
     * Adds a BomLine to this Bom and sets the relationship.
     *
     * @param bomLine the BomLine to add
     */
    public void addBomLine(BomLine bomLine) {
        bomLines.add(bomLine);
        bomLine.setBom(this);
    }

    /**
     * Removes a BomLine from this Bom and clears the relationship.
     *
     * @param bomLine the BomLine to remove
     */
    public void removeBomLine(BomLine bomLine) {
        bomLines.remove(bomLine);
        bomLine.setBom(null);
    }
}
