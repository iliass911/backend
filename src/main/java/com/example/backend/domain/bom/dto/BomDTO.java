package com.example.backend.domain.bom.dto;

import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomDTO {
    private Long id;
    private Long boardId;
    private Double totalCost;
    private Set<BomLineDTO> bomLines;
}