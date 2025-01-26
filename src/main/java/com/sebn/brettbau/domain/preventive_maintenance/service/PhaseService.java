// PhaseService.java
package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.PhaseDTO;
import java.util.List;

public interface PhaseService {
    List<PhaseDTO> getAllPhases();
    PhaseDTO getPhaseById(Long id);
    PhaseDTO createPhase(PhaseDTO dto);
    PhaseDTO updatePhase(Long id, PhaseDTO dto);
    void deletePhase(Long id);
}
