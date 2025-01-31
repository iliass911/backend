package com.sebn.brettbau.domain.bom.entity;

import lombok.*;
import javax.persistence.*; // Updated import
// import javax.persistence.*; // Remove or comment out if present
import com.sebn.brettbau.domain.bom.entity.BomLine;

@Entity
@Table(name = "bom_line_units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomLineUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_line_id", nullable = false)
    private BomLine bomLine;

    @Column(name = "unit_index", nullable = false)
    private Integer unitIndex;

    @Column(name = "unit_name", nullable = false)
    private String unitName;
}

