package com.sebn.brettbau.domain.customtable.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomTableDTO {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private List<CustomColumnDTO> columns;
    // New field: 2D list representing the table's cell data.
    private List<List<Object>> data;
}
