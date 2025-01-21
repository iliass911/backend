// src/main/java/com/example/backend/domain/bom/entity/Bom.java
package com.sebn.brettbau.domain.bom.entity;

import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
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
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BomLine> bomLines = new HashSet<>();

    // Stores the total cost of the BOM (calculated from lines)
    private Double totalCost;
}
