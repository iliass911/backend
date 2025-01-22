package com.sebn.brettbau.domain.preventive_maintenance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "board_families")
@Getter
@Setter
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
    private Integer boardCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private Set<Board> boards = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (boardCount == null) {
            boardCount = 0;
        }
    }

    public void addBoard(Board board) {
        boards.add(board);
        board.setFamily(this);
        boardCount = boards.size();
    }

    public void removeBoard(Board board) {
        boards.remove(board);
        board.setFamily(null);
        boardCount = boards.size();
    }
}