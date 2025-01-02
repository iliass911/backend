// src/main/java/com/example/backend/domain/preventive_maintenance/mapper/PackMapper.java

package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.PackDTO;
import com.example.backend.domain.preventive_maintenance.entity.Pack;
import com.example.backend.domain.preventive_maintenance.entity.Site;
import com.example.backend.domain.preventive_maintenance.repository.SiteRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PackMapper {

    @Autowired
    protected SiteRepository siteRepository;

    @Mapping(source = "site.id", target = "siteId")
    public abstract PackDTO toDTO(Pack pack);

    @Mapping(target = "site", expression = "java(getSite(packDTO.getSiteId()))")
    public abstract Pack toEntity(PackDTO packDTO);

    protected Site getSite(Long siteId) {
        return siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Site not found with id " + siteId));
    }
}
