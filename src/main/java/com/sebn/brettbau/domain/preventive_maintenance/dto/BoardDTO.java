package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.*;
import java.time.LocalDate;

/**
 * Data Transfer Object for Board entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private Long id;
    private String boardNumber; // Unique identifier
    private String fbId;
    private String projet;
    private String fbName;
    private String fbSize;
    private String firstTechLevel;
    private String plant;
    private String inUse;
    private Boolean testClip;
    private String area;
    private String fbType1;
    private String fbType2;
    private String fbType3;
    private String side;
    private String derivate;
    private String creationReason;
    private LocalDate firstYellowReleaseDate;
    private LocalDate firstOrangeReleaseDate;
    private LocalDate firstGreenReleaseDate;
    private LocalDate firstUseByProdDate;
    private String currentTechLevel;
    private String nextTechLevel;
    private String lastTechChangeImplemented;
    private LocalDate lastTechChangeImpleDate;
    private LocalDate lastTechChangeReleaseDate;
    private String comment1;
    private String comment2;
    private String comment3;
    private Long packId;
    private Long assignedUserId;
    private Double cost;
    private Integer quantity;
    private String storagePlace; // If you keep it
    private LocalDate creationDate;
    private String status;
}
