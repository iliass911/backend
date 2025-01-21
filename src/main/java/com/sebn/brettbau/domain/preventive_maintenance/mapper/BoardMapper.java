package com.sebn.brettbau.domain.preventive_maintenance.mapper;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for Board entity and BoardDTO using MapStruct.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BoardMapper {

    // Entity -> DTO
    @Mapping(source = "pack.id", target = "packId")
    @Mapping(source = "assignedUser.id", target = "assignedUserId")
    BoardDTO toDTO(Board board);

    // DTO -> Entity (for creating a new entity)
    @Mapping(target = "pack", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "checklists", ignore = true)
    @Mapping(target = "bom", ignore = true)
    Board toEntity(BoardDTO boardDTO);

    // Update existing entity from DTO (ignores null fields and certain relationships)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pack", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "checklists", ignore = true)
    @Mapping(target = "bom", ignore = true)
    void updateEntityFromDto(BoardDTO dto, @MappingTarget Board entity);
}
