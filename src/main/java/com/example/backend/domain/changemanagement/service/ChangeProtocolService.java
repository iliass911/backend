// src/main/java/com/example/backend/domain/changemanagement/service/ChangeProtocolService.java
package com.example.backend.domain.changemanagement.service;

import com.example.backend.domain.changemanagement.dto.ChangeProtocolDTO;
import com.example.backend.domain.changemanagement.entity.ChangeProtocol;
import com.example.backend.domain.changemanagement.repository.ChangeProtocolRepository;
import com.example.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangeProtocolService {
    private final ChangeProtocolRepository repository;

    public ChangeProtocolDTO create(ChangeProtocolDTO dto) {
        ChangeProtocol entity = mapToEntity(dto);
        ChangeProtocol saved = repository.save(entity);
        return mapToDTO(saved);
    }

    public ChangeProtocolDTO getById(Long id) {
        ChangeProtocol entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ChangeProtocol not found with id: " + id));
        return mapToDTO(entity);
    }

    public List<ChangeProtocolDTO> getAll() {
        return repository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public ChangeProtocolDTO update(Long id, ChangeProtocolDTO dto) {
        ChangeProtocol existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ChangeProtocol not found with id: " + id));
        
        // Update fields
        existing.setChaine(dto.getChaine());
        existing.setPanneauModule(dto.getPanneauModule());
        existing.setNumeroPanneauStart(dto.getNumeroPanneauStart());
        existing.setNumeroPanneauEnd(dto.getNumeroPanneauEnd());
        existing.setModification(dto.getModification());
        existing.setBrettbau(dto.getBrettbau());
        existing.setDateBrettbau(dto.getDateBrettbau());
        existing.setPqmValidation(dto.getPqmValidation());
        existing.setDatePqm(dto.getDatePqm());
        existing.setPprValidation(dto.getPprValidation());
        existing.setDatePpr(dto.getDatePpr());
        existing.setRemark(dto.getRemark());
        existing.setRemarque(dto.getRemarque());
        // ... update other fields as necessary

        ChangeProtocol updated = repository.save(existing);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChangeProtocol not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Mapping methods between Entity and DTO (could use a Mapper library)
    private ChangeProtocolDTO mapToDTO(ChangeProtocol entity) {
        ChangeProtocolDTO dto = new ChangeProtocolDTO();
        dto.setId(entity.getId());
        dto.setChaine(entity.getChaine());
        dto.setPanneauModule(entity.getPanneauModule());
        dto.setNumeroPanneauStart(entity.getNumeroPanneauStart());
        dto.setNumeroPanneauEnd(entity.getNumeroPanneauEnd());
        dto.setModification(entity.getModification());
        dto.setBrettbau(entity.getBrettbau());
        dto.setDateBrettbau(entity.getDateBrettbau());
        dto.setPqmValidation(entity.getPqmValidation());
        dto.setDatePqm(entity.getDatePqm());
        dto.setPprValidation(entity.getPprValidation());
        dto.setDatePpr(entity.getDatePpr());
        dto.setRemark(entity.getRemark());
        dto.setRemarque(entity.getRemarque());
        // map other fields if needed
        return dto;
    }

    private ChangeProtocol mapToEntity(ChangeProtocolDTO dto) {
        return ChangeProtocol.builder()
                .chaine(dto.getChaine())
                .panneauModule(dto.getPanneauModule())
                .numeroPanneauStart(dto.getNumeroPanneauStart())
                .numeroPanneauEnd(dto.getNumeroPanneauEnd())
                .modification(dto.getModification())
                .brettbau(dto.getBrettbau())
                .dateBrettbau(dto.getDateBrettbau())
                .pqmValidation(dto.getPqmValidation())
                .datePqm(dto.getDatePqm())
                .pprValidation(dto.getPprValidation())
                .datePpr(dto.getDatePpr())
                .remark(dto.getRemark())
                .remarque(dto.getRemarque())
                // set other fields as necessary
                .build();
    }
}
