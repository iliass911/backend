package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteDTO {
    private Long id;
    private String name;
    private String location;
}
 
