package com.sebn.brettbau.domain.movement.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for BoardMovement
 */
@Data
public class BoardMovementDTO {
    private Long id;
    private Long boardId;        // or boardNumber, whichever you prefer
    private String fromLocation;
    private String toLocation;
    private String movedBy;
    private LocalDateTime moveDate;
    private String reason;
}
