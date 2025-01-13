package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.example.backend.domain.preventive_maintenance.entity.BoardFamily;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import org.mapstruct.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BoardFamilyMapper {
    
    default List<Long> boardsToIds(Set<Board> boards) {
        if (boards == null) {
            return null;
        }
        return boards.stream()
                .map(board -> board.getId())
                .collect(Collectors.toList());
    }

    @Mapping(target = "boardIds", source = "boards", qualifiedByName = "boardsToIds")
    BoardFamilyDTO toDTO(BoardFamily entity);

    @Mapping(target = "boards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    BoardFamily toEntity(BoardFamilyDTO dto);

    @Named("boardsToIds")
    default List<Long> mapBoardsToIds(Set<Board> boards) {
        if (boards == null) {
            return null;
        }
        return boards.stream()
                .map(board -> board.getId())
                .collect(Collectors.toList());
    }
}