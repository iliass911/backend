package com.sebn.brettbau.domain.board.dto;

import lombok.Data;
import java.util.List;

@Data
public class BoardFamilyComparisonDTO {
    private BoardFamilyDTO family1;
    private BoardFamilyDTO family2;
    private List<BomComparisonItemDTO> differences;
}