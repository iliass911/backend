package com.sebn.brettbau.domain.maintenance.mapper;

import com.sebn.brettbau.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.sebn.brettbau.domain.maintenance.entity.MaintenanceIntervention;

public class MaintenanceInterventionMapper {

    public static MaintenanceInterventionDTO toDTO(MaintenanceIntervention entity) {
        if (entity == null) return null;
        MaintenanceInterventionDTO dto = new MaintenanceInterventionDTO();
        dto.setId(entity.getId());
        dto.setTypeIntervention(entity.getTypeIntervention());
        dto.setValeurEuro(entity.getValeurEuro());
        dto.setPointExaminer(entity.getPointExaminer());
        dto.setNatureIntervention(entity.getNatureIntervention());
        dto.setPosteTouche(entity.getPosteTouche());
        dto.setZone(entity.getZone());
        dto.setShift(entity.getShift());
        dto.setNumeroPanneau(entity.getNumeroPanneau());
        dto.setNomPrenomBB(entity.getNomPrenomBB());
        dto.setTempsIntervention(entity.getTempsIntervention());
        dto.setDate(entity.getDate());
        dto.setCommentaire(entity.getCommentaire());
        dto.setSite(entity.getSite());
        return dto;
    }

    public static MaintenanceIntervention toEntity(MaintenanceInterventionDTO dto) {
        if (dto == null) return null;
        return MaintenanceIntervention.builder()
                .id(dto.getId())
                .typeIntervention(dto.getTypeIntervention())
                .valeurEuro(dto.getValeurEuro())
                .pointExaminer(dto.getPointExaminer())
                .natureIntervention(dto.getNatureIntervention())
                .posteTouche(dto.getPosteTouche())
                .zone(dto.getZone())
                .shift(dto.getShift())
                .numeroPanneau(dto.getNumeroPanneau())
                .nomPrenomBB(dto.getNomPrenomBB())
                .tempsIntervention(dto.getTempsIntervention())
                .date(dto.getDate())
                .commentaire(dto.getCommentaire())
                .site(dto.getSite())
                .build();
    }
}
