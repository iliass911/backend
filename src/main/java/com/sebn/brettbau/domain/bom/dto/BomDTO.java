// src/main/java/com/example/backend/domain/bom/dto/BomDTO.java
package com.sebn.brettbau.domain.bom.dto;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomDTO {
    private Long id;
    private Long boardId;  // ID of Board
    private Double totalCost;
    private Set<BomLineDTO> bomLines; // Child lines
}

