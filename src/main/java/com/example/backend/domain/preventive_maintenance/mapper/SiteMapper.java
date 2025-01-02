package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.SiteDTO;
import com.example.backend.domain.preventive_maintenance.entity.Site;

public class SiteMapper {
    public static SiteDTO toDTO(Site site) {
        if (site == null) return null;
        return SiteDTO.builder()
                .id(site.getId())
                .name(site.getName())
                .location(site.getLocation())
                .build();
    }

    public static Site toEntity(SiteDTO dto) {
        if (dto == null) return null;
        return Site.builder()
                .id(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
    }
}
