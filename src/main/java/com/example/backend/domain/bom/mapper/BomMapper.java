package com.example.backend.domain.bom.mapper;

import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.entity.Bom;
import com.example.backend.domain.bom.entity.BomLine;
import com.example.backend.domain.bom.entity.BomLineUnit;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class BomMapper {

    @Mapping(target = "boardId", source = "board.id")
    @Mapping(target = "bomLines", expression = "java(mapBomLinesToDTOs(entity.getBomLines()))")
    public abstract BomDTO toDTO(Bom entity);

    @Mapping(target = "board", ignore = true)
    @Mapping(target = "bomLines", ignore = true)
    public abstract Bom toEntity(BomDTO dto);

    protected Set<BomLineDTO> mapBomLinesToDTOs(Set<BomLine> lines) {
        if (lines == null) return null;
        return lines.stream()
                .map(line -> BomLineDTO.builder()
                        .id(line.getId())
                        .bomId(line.getBom().getId())
                        .inventoryItemId(line.getInventoryItem() != null ? line.getInventoryItem().getId() : null)
                        .category(line.getCategory())
                        .componentName(line.getComponentName())
                        .unitPrice(line.getUnitPrice())
                        .quantity(line.getQuantity())
                        .lineCost(line.getLineCost())
                        .unitNames(line.getUnits().stream()
                                .map(BomLineUnit::getUnitName)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toSet());
    }
}