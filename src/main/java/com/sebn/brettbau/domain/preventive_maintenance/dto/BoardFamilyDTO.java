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
    private Integer boardCount;
    private LocalDateTime createdAt;
    private List<Long> boardIds;
}