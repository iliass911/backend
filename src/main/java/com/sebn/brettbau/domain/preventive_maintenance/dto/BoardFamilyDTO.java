package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFamilyDTO {
    private Long id;
    private String familyName;
    private String projet;
    private String side;
    private String fbType2;
    private String fbType3;
    private String fbSize;
    private String derivate;
    private Integer boardCount;
    private LocalDateTime createdAt;
    private List<Long> boardIds;
}