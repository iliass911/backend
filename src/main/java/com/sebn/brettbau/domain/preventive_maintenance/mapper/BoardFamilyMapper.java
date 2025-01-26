package com.sebn.brettbau.domain.preventive_maintenance.mapper;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BoardFamilyMapper {

    /**
     * Converts a BoardFamily entity to a BoardFamilyDTO.
     *
     * @param family The BoardFamily entity.
     * @return The BoardFamilyDTO.
     */
    public BoardFamilyDTO toDTO(BoardFamily family) {
        if (family == null) {
            return null;
        }

        BoardFamilyDTO dto = new BoardFamilyDTO();
        dto.setId(family.getId());
        dto.setFamilyName(family.getFamilyName());
        dto.setProjet(family.getProjet());
        dto.setSide(family.getSide());
        dto.setFbType2(family.getFbType2());
        dto.setFbType3(family.getFbType3());
        dto.setFbSize(family.getFbSize());
        dto.setDerivate(family.getDerivate());
        dto.setBoardCount(family.getBoardCount());
        dto.setCreatedAt(family.getCreatedAt() != null ? family.getCreatedAt().toString() : null);

        return dto;
    }

    /**
     * Converts a BoardFamilyDTO to a BoardFamily entity.
     *
     * @param dto The BoardFamilyDTO.
     * @return The BoardFamily entity.
     */
    public BoardFamily toEntity(BoardFamilyDTO dto) {
        if (dto == null) {
            return null;
        }

        LocalDateTime createdAt;
        try {
            createdAt = dto.getCreatedAt() != null ? 
                LocalDateTime.parse(dto.getCreatedAt()) : 
                LocalDateTime.now();
        } catch (Exception e) {
            // Optionally log the exception for debugging
            createdAt = LocalDateTime.now();
        }

        return BoardFamily.builder()
                .id(dto.getId())
                .familyName(dto.getFamilyName())
                .projet(dto.getProjet())
                .side(dto.getSide())
                .fbType2(dto.getFbType2())
                .fbType3(dto.getFbType3())
                .fbSize(dto.getFbSize())
                .derivate(dto.getDerivate())
                .boardCount(dto.getBoardCount() != null ? dto.getBoardCount() : 0)
                .createdAt(createdAt)
                .build();
    }
}
