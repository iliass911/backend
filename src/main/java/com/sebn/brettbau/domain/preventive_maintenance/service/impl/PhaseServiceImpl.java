// PhaseServiceImpl.java
package com.sebn.brettbau.domain.preventive_maintenance.service.impl;

import com.sebn.brettbau.domain.preventive_maintenance.dto.PhaseDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Phase;
import com.sebn.brettbau.domain.preventive_maintenance.repository.PhaseRepository;
import com.sebn.brettbau.domain.preventive_maintenance.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;

    @Autowired
    public PhaseServiceImpl(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    @Override
    public List<PhaseDTO> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PhaseDTO getPhaseById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found with ID: " + id));
        return convertToDTO(phase);
    }

    @Override
    public PhaseDTO createPhase(PhaseDTO dto) {
        Phase entity = convertToEntity(dto);
        Phase saved = phaseRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    public PhaseDTO updatePhase(Long id, PhaseDTO dto) {
        Phase existing = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found with ID: " + id));
        existing.setName(dto.getName());
        existing.setProjectId(dto.getProjectId());
        Phase updated = phaseRepository.save(existing);
        return convertToDTO(updated);
    }

    @Override
    public void deletePhase(Long id) {
        phaseRepository.deleteById(id);
    }

    private PhaseDTO convertToDTO(Phase entity) {
        PhaseDTO dto = new PhaseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProjectId(entity.getProjectId());
        return dto;
    }

    private Phase convertToEntity(PhaseDTO dto) {
        return Phase.builder()
                .id(dto.getId())
                .name(dto.getName())
                .projectId(dto.getProjectId())
                .build();
    }
}

