package com.sebn.brettbau.domain.customtable.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomTableDataDTO {
    private Long id;
    private Long tableId;
    private Long columnId;
    private Integer rowIndex;
    private String cellValue;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
}
