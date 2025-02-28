package com.sebn.brettbau.domain.movement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a movement of a board from one location to another.
 */
@Entity
@Table(name = "board_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // If you have a Board entity, you might do:
    // @ManyToOne
    // @JoinColumn(name = "board_id")
    // private Board board;
    // Otherwise, just store the boardId or boardNumber
    private Long boardId;

    private String fromLocation;
    private String toLocation;

    private String movedBy;        // e.g. user who performed the move
    private LocalDateTime moveDate; // date/time of movement

    private String reason;         // reason for movement
}
