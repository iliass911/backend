package com.sebn.brettbau.domain.board.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bom_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_family_id", nullable = false)
    private BoardFamily boardFamily;
    
    @Column(nullable = false)
    private String segment;
    
    @Column(nullable = false)
    private String kurzname;
    
    @Column(name = "ident_matchcode", nullable = false)
    private String identMatchcode;
    
    @Column(name = "model_type")
    private String modelType; // 2D/3D
    
    @Column(name = "sesam_number")
    private String sesamNumber;
    
    @Column(name = "missing_on_board")
    private Boolean missingOnBoard;
    
    @Column(name = "quantity_on_board")
    private Integer quantityOnBoard;
    
    private String observation;
    private Double price;
    
    @Column(name = "motif")
    private String motif;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}