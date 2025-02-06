package com.sebn.brettbau.domain.customtable.dto;

import lombok.Data;

@Data
public class CustomColumnDTO {
    private Long id;
    private String name;
    private String type;
    private Integer orderIndex;
    private Boolean required;
    private String defaultValue;
    private Integer precision;
    private Integer scale;
    private Integer maxLength;
    private String dateFormat;
}
