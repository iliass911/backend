package com.sebn.brettbau.domain.board.dto;

import lombok.Data;
import java.util.List;

@Data
public class BomComparisonItemDTO {
    private BomItemDTO item1;          // BOM item from family 1 (null if added in family2)
    private BomItemDTO item2;          // BOM item from family 2 (null if removed from family1)
    private String comparisonType;     // "ADDED", "REMOVED", or "MODIFIED"
    private List<String> modifiedFields; // For MODIFIED items, list of fields that differ
}
