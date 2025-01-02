package com.example.backend.domain.preventive_maintenance.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    // Instead of "id", you could name it something else, but typically in DTOs
    // we keep an ID field for referencing the object. If you prefer “boardNumber” or “n°”, do so:
    private Long id;  

    @NotBlank(message = "FB Name is required")
    private String fbName;              // old: name

    @NotBlank(message = "FB Size is required")
    private String fbSize;              // old: dimensions

    private String firstTechLevel;      // old: technicalLevel
    private String projet;              // old: project
    private String plant;               // old: site
    private String storagePlace;        // kept as is
    private String inUse;               // old: situation
    private Boolean testClip;           // old: hasTestClip
    private String area;                // old: zone
    private String fbType1;             // old: boardType
    private String side;                // old: boardSide
    private String comment1;            // old: comment

    private LocalDate firstYellowReleaseDate; // old: firstReleaseDate
    private LocalDate creationDate;           // old: constructionDate
    private String fbId;                     // old: dtes

    @DecimalMin(value = "0.0", message = "Cost must be non-negative")
    private Double cost;               // keep as cost

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;          // keep as quantity

    // Relationship IDs stay as is
    private Long packId;
    private Long assignedUserId;

    // New fields (if you actually want them in the DTO):
    private String derivate;  
    private String fbType2;
    private String fbType3;
    private String creationReason;
    private LocalDate firstOrangeReleaseDate;
    private LocalDate firstGreenReleaseDate;
    private LocalDate firstUseByProdDate;
    private String currentTechLevel;
    private String nextTechLevel;
    private String lastTechChangeImplemented;
    private LocalDate lastTechChangeImpleDate;
    private LocalDate lastTechChangeReleaseDate;
    private String comment2;
    private String comment3;
}
