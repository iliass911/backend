package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.ChecklistDTO;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import com.example.backend.domain.preventive_maintenance.entity.Checklist;

public class ChecklistMapper {
    public static ChecklistDTO toDTO(Checklist checklist) {
        return ChecklistDTO.builder()
                .id(checklist.getId())
                .boardId(checklist.getBoard().getId())
                .technicianName(checklist.getTechnicianName())
                .qualityAgentName(checklist.getQualityAgentName())
                .completionPercentage(checklist.getCompletionPercentage())
                .comments(checklist.getComments())
                .qualityValidated(checklist.getQualityValidated())
                .validationDate(checklist.getValidationDate())
                .expiryDate(checklist.getExpiryDate())
                .workStatus(checklist.getWorkStatus())
                .createdAt(checklist.getCreatedAt())
                .weekNumber(checklist.getWeekNumber())  // Mapping new field
                .build();
    }

    public static Checklist toEntity(ChecklistDTO dto, Board board) {
        return Checklist.builder()
                .board(board)
                .technicianName(dto.getTechnicianName())
                .qualityAgentName(dto.getQualityAgentName())
                .completionPercentage(dto.getCompletionPercentage())
                .comments(dto.getComments())
                .qualityValidated(dto.getQualityValidated())
                .validationDate(dto.getValidationDate())
                .expiryDate(dto.getExpiryDate())
                .workStatus(dto.getWorkStatus())
                .weekNumber(dto.getWeekNumber())  // Mapping new field
                .build();
    }
}
