package com.sebn.brettbau.domain.bom.mapper;

import com.sebn.brettbau.domain.bom.dto.BomDTO;
import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.bom.entity.Bom;
import com.sebn.brettbau.domain.bom.entity.BomLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class BomMapper {

    /**
     * Maps a Bom entity to a BomDTO.
     *
     * @param entity the Bom entity to map
     * @return the mapped BomDTO
     */
    @Mapping(target = "boardId", source = "board.id")
    @Mapping(target = "bomLines", expression = "java(mapBomLinesToDTOs(entity.getBomLines()))")
    public abstract BomDTO toDTO(Bom entity);

    /**
     * Maps a BomDTO to a Bom entity.
     *
     * @param dto the BomDTO to map
     * @return the mapped Bom entity
     */
    @Mapping(target = "board", ignore = true) // Set in service
    @Mapping(target = "bomLines", ignore = true) // Set in service
    public abstract Bom toEntity(BomDTO dto);

    /**
     * Custom mapping method to convert a set of BomLine entities to a set of BomLineDTOs,
     * converting Long IDs to String IDs to handle both temporary and permanent IDs.
     *
     * @param lines the set of BomLine entities
     * @return the set of BomLineDTOs
     */
    protected Set<BomLineDTO> mapBomLinesToDTOs(Set<BomLine> lines) {
        if (lines == null) return null;
        return lines.stream()
                .map(line -> BomLineDTO.builder()
                        .id(line.getId() != null ? line.getId().toString() : null) // Convert Long to String with null check
                        .bomId(line.getBom().getId())
                        .inventoryItemId(line.getInventoryItem() != null ? line.getInventoryItem().getId() : null)
                        .category(line.getCategory())
                        .componentName(line.getComponentName())
                        .unitPrice(line.getUnitPrice())
                        .quantity(line.getQuantity())
                        .lineCost(line.getLineCost())
                        .unitNames(line.getUnitNames()) // Included unitNames in mapping
                        .build())
                .collect(Collectors.toSet());
    }
}

