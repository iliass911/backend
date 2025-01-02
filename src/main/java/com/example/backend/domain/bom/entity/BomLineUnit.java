package com.example.backend.domain.bom.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bom_line_units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "bomLine")
public class BomLineUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bom_line_id", nullable = false)
    private BomLine bomLine;

    @Column(nullable = false)
    private String unitName;

    @Column(nullable = false)
    private Integer unitIndex;

    @PrePersist
    @PreUpdate
    private void validateUnitName() {
        if (unitName == null || unitName.trim().isEmpty()) {
            unitName = "Unit " + unitIndex;
        }
    }
}