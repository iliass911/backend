package com.sebn.brettbau.domain.board.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Data
public class BoardFamilyDTO {
    private Long id;
    
    @NotBlank(message = "Family name is required")
    private String familyName;
    
    @NotBlank(message = "Project is required")
    private String project;
    
    @NotBlank(message = "Side is required")
    private String side;
    
    @NotBlank(message = "Derivate is required")
    private String derivate;
    
    @NotNull(message = "Number of boards is required")
    @Min(value = 1, message = "Number of boards must be at least 1")
    private Integer numberOfBoards;
    
    @NotBlank(message = "Phase is required")
    private String phase;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BomItemDTO> bomItems;
}