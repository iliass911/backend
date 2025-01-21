package com.sebn.brettbau.domain.action.service;

import com.sebn.brettbau.domain.action.dto.ActionRecordDTO;
import com.sebn.brettbau.domain.action.entity.ActionRecord;
import com.sebn.brettbau.domain.action.repository.ActionRecordRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActionRecordService {
    private final ActionRecordRepository repository;

    private ActionRecordDTO mapToDTO(ActionRecord entity) {
        if(entity == null) return null;
        ActionRecordDTO dto = new ActionRecordDTO();
        dto.setId(entity.getId());
        dto.setAction(entity.getAction());
        dto.setPriority(entity.getPriority());
        dto.setResponsable(entity.getResponsable());
        dto.setProjet(entity.getProjet());
        dto.setPhaseProjet(entity.getPhaseProjet());
        dto.setTypeIntervention(entity.getTypeIntervention());
        dto.setStartDate(entity.getStartDate());
        dto.setFinishDate(entity.getFinishDate());
        dto.setKw(entity.getKw());
        dto.setMois(entity.getMois());
        dto.setMatricule(entity.getMatricule());
        dto.setStatut(entity.getStatut());
        dto.setRemarque(entity.getRemarque());
        return dto;
    }

    private ActionRecord mapToEntity(ActionRecordDTO dto) {
        if(dto == null) return null;
        return ActionRecord.builder()
                .id(dto.getId())
                .action(dto.getAction())
                .priority(dto.getPriority())
                .responsable(dto.getResponsable())
                .projet(dto.getProjet())
                .phaseProjet(dto.getPhaseProjet())
                .typeIntervention(dto.getTypeIntervention())
                .startDate(dto.getStartDate())
                .finishDate(dto.getFinishDate())
                .kw(dto.getKw())
                .mois(dto.getMois())
                .matricule(dto.getMatricule())
                .statut(dto.getStatut())
                .remarque(dto.getRemarque())
                .build();
    }

    public List<ActionRecordDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ActionRecordDTO getById(Long id) {
        ActionRecord entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ActionRecord not found with id: " + id));
        return mapToDTO(entity);
    }

    public ActionRecordDTO create(ActionRecordDTO dto) {
        ActionRecord entity = mapToEntity(dto);
        ActionRecord saved = repository.save(entity);
        return mapToDTO(saved);
    }

    public ActionRecordDTO update(Long id, ActionRecordDTO dto) {
        ActionRecord existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ActionRecord not found with id: " + id));

        // Update fields based on dto; assuming partial updates are allowed
        existing.setAction(dto.getAction());
        existing.setPriority(dto.getPriority());
        existing.setResponsable(dto.getResponsable());
        existing.setProjet(dto.getProjet());
        existing.setPhaseProjet(dto.getPhaseProjet());
        existing.setTypeIntervention(dto.getTypeIntervention());
        existing.setStartDate(dto.getStartDate());
        existing.setFinishDate(dto.getFinishDate());
        existing.setKw(dto.getKw());
        existing.setMois(dto.getMois());
        existing.setMatricule(dto.getMatricule());
        existing.setStatut(dto.getStatut());
        existing.setRemarque(dto.getRemarque());

        ActionRecord updated = repository.save(existing);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ActionRecord not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
