package com.sebn.brettbau.domain.board.dto;

import lombok.Data;
import java.util.List;

@Data
public class BomComparisonItemDTO {
    private BomItemDTO item1;
    private BomItemDTO item2;
    private String comparisonType; // ADDED, REMOVED, MODIFIED
    private List<String> modifiedFields;
}