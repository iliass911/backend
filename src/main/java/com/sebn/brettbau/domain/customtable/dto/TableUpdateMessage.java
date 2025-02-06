package com.sebn.brettbau.domain.customtable.dto;

import lombok.Data;

@Data
public class TableUpdateMessage {
    private Long tableId;
    private Long columnId;
    private Integer rowIndex;
    private String newValue;
    private String updatedBy;
    private UpdateType updateType;

    public enum UpdateType {
        CELL_UPDATE,
        ROW_INSERT,
        ROW_DELETE,
        COLUMN_UPDATE
    }
}
