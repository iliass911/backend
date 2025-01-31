package com.sebn.brettbau.domain.maintenance.service;

import com.sebn.brettbau.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.sebn.brettbau.domain.maintenance.entity.MaintenanceIntervention;
import com.sebn.brettbau.domain.maintenance.mapper.MaintenanceInterventionMapper;
import com.sebn.brettbau.domain.maintenance.repository.MaintenanceInterventionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceInterventionService {

    private final MaintenanceInterventionRepository repository;

    public MaintenanceInterventionService(MaintenanceInterventionRepository repository) {
        this.repository = repository;
    }

    public MaintenanceInterventionDTO createIntervention(MaintenanceInterventionDTO dto) {
        MaintenanceIntervention entity = MaintenanceInterventionMapper.toEntity(dto);
        MaintenanceIntervention saved = repository.save(entity);
        return MaintenanceInterventionMapper.toDTO(saved);
    }

    public List<MaintenanceInterventionDTO> getAllInterventions() {
        return repository.findAll().stream()
                .map(MaintenanceInterventionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MaintenanceInterventionDTO getInterventionById(Long id) {
        return repository.findById(id)
                .map(MaintenanceInterventionMapper::toDTO)
                .orElse(null);
    }

    public MaintenanceInterventionDTO updateIntervention(Long id, MaintenanceInterventionDTO dto) {
        MaintenanceIntervention entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null; // or throw NotFoundException in a real app
        }
        entity.setTypeIntervention(dto.getTypeIntervention());
        entity.setValeurEuro(dto.getValeurEuro());
        entity.setPointExaminer(dto.getPointExaminer());
        entity.setNatureIntervention(dto.getNatureIntervention());
        entity.setPosteTouche(dto.getPosteTouche());
        entity.setZone(dto.getZone());
        entity.setShift(dto.getShift());
        entity.setNumeroPanneau(dto.getNumeroPanneau());
        entity.setNomPrenomBB(dto.getNomPrenomBB());
        entity.setTempsIntervention(dto.getTempsIntervention());
        entity.setDate(dto.getDate());
        entity.setCommentaire(dto.getCommentaire());
        entity.setSite(dto.getSite());
        MaintenanceIntervention updated = repository.save(entity);
        return MaintenanceInterventionMapper.toDTO(updated);
    }

    public void deleteIntervention(Long id) {
        repository.deleteById(id);
    }
}

