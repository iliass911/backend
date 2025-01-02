package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // Integrate with Spring
public interface BoardMapper {

    // Entity -> DTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fbName", target = "fbName")
    @Mapping(source = "fbSize", target = "fbSize")
    @Mapping(source = "firstTechLevel", target = "firstTechLevel")
    @Mapping(source = "projet", target = "projet")
    @Mapping(source = "plant", target = "plant")
    @Mapping(source = "storagePlace", target = "storagePlace")
    @Mapping(source = "inUse", target = "inUse")
    @Mapping(source = "testClip", target = "testClip")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "fbType1", target = "fbType1")
    @Mapping(source = "side", target = "side")
    @Mapping(source = "comment1", target = "comment1")
    @Mapping(source = "firstYellowReleaseDate", target = "firstYellowReleaseDate")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "fbId", target = "fbId")
    @Mapping(source = "cost", target = "cost")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "pack.id", target = "packId")
    @Mapping(source = "assignedUser.id", target = "assignedUserId")
    // New fields
    @Mapping(source = "derivate", target = "derivate")
    @Mapping(source = "fbType2", target = "fbType2")
    @Mapping(source = "fbType3", target = "fbType3")
    @Mapping(source = "creationReason", target = "creationReason")
    @Mapping(source = "firstOrangeReleaseDate", target = "firstOrangeReleaseDate")
    @Mapping(source = "firstGreenReleaseDate", target = "firstGreenReleaseDate")
    @Mapping(source = "firstUseByProdDate", target = "firstUseByProdDate")
    @Mapping(source = "currentTechLevel", target = "currentTechLevel")
    @Mapping(source = "nextTechLevel", target = "nextTechLevel")
    @Mapping(source = "lastTechChangeImplemented", target = "lastTechChangeImplemented")
    @Mapping(source = "lastTechChangeImpleDate", target = "lastTechChangeImpleDate")
    @Mapping(source = "lastTechChangeReleaseDate", target = "lastTechChangeReleaseDate")
    @Mapping(source = "comment2", target = "comment2")
    @Mapping(source = "comment3", target = "comment3")
    BoardDTO toDTO(Board board);

    // DTO -> Entity
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fbName", target = "fbName")
    @Mapping(source = "fbSize", target = "fbSize")
    @Mapping(source = "firstTechLevel", target = "firstTechLevel")
    @Mapping(source = "projet", target = "projet")
    @Mapping(source = "plant", target = "plant")
    @Mapping(source = "storagePlace", target = "storagePlace")
    @Mapping(source = "inUse", target = "inUse")
    @Mapping(source = "testClip", target = "testClip")
    @Mapping(source = "area", target = "area")
    @Mapping(source = "fbType1", target = "fbType1")
    @Mapping(source = "side", target = "side")
    @Mapping(source = "comment1", target = "comment1")
    @Mapping(source = "firstYellowReleaseDate", target = "firstYellowReleaseDate")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "fbId", target = "fbId")
    @Mapping(source = "cost", target = "cost")
    @Mapping(source = "quantity", target = "quantity")
    // Relationships
    @Mapping(target = "pack", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "checklists", ignore = true)
    // New fields
    @Mapping(source = "derivate", target = "derivate")
    @Mapping(source = "fbType2", target = "fbType2")
    @Mapping(source = "fbType3", target = "fbType3")
    @Mapping(source = "creationReason", target = "creationReason")
    @Mapping(source = "firstOrangeReleaseDate", target = "firstOrangeReleaseDate")
    @Mapping(source = "firstGreenReleaseDate", target = "firstGreenReleaseDate")
    @Mapping(source = "firstUseByProdDate", target = "firstUseByProdDate")
    @Mapping(source = "currentTechLevel", target = "currentTechLevel")
    @Mapping(source = "nextTechLevel", target = "nextTechLevel")
    @Mapping(source = "lastTechChangeImplemented", target = "lastTechChangeImplemented")
    @Mapping(source = "lastTechChangeImpleDate", target = "lastTechChangeImpleDate")
    @Mapping(source = "lastTechChangeReleaseDate", target = "lastTechChangeReleaseDate")
    @Mapping(source = "comment2", target = "comment2")
    @Mapping(source = "comment3", target = "comment3")
    Board toEntity(BoardDTO boardDTO);
}
