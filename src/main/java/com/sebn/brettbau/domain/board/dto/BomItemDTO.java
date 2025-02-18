package com.sebn.brettbau.domain.board.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BomItemDTO {
    private Long id;
    
    @NotBlank(message = "Segment is required")
    private String segment;
    
    @NotBlank(message = "Kurzname is required")
    private String kurzname;
    
    @NotBlank(message = "Ident/Matchcode is required")
    private String identMatchcode;
    
    private String modelType;
    private String sesamNumber;
    private Boolean missingOnBoard;
    
    @NotNull(message = "Quantity is required")
    private Integer quantityOnBoard;
    
    private String observation;
    private Double price;
    
    private Long boardFamilyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
