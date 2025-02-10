package com.sebn.brettbau.domain.board.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_families")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String familyName;
    
    @Column(nullable = false)
    private String project;
    
    @Column(nullable = false)
    private String side;
    
    @Column(nullable = false)
    private String derivate;
    
    @Column(nullable = false)
    private Integer numberOfBoards;
    
    @Column(nullable = false)
    private String phase;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "boardFamily", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BomItem> bomItems = new ArrayList<>();
    
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