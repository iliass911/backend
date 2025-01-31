// src/main/java/com/sebn/brettbau/domain/preventive_maintenance/service/BoardFamilyService.java

package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.bom.entity.BomLine;
import com.sebn.brettbau.domain.bom.service.BomService;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardFamilyRepository;
import com.sebn.brettbau.domain.bom.repository.BomLineRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardFamilyService {

    private final BoardFamilyRepository boardFamilyRepository;
    private final BomLineRepository bomLineRepository;
    private final BomService bomService; // Inject BomService

    @Autowired
    public BoardFamilyService(BoardFamilyRepository boardFamilyRepository,
                              BomLineRepository bomLineRepository,
                              BomService bomService) {
        this.boardFamilyRepository = boardFamilyRepository;
        this.bomLineRepository = bomLineRepository;
        this.bomService = bomService;
    }

    /**
     * Creates a new BoardFamily with the provided details.
     *
     * @param familyName The name of the family.
     * @param projet     The project name.
     * @param side       The side information.
     * @param fbType2    FB Type 2.
     * @param fbType3    FB Type 3.
     * @param fbSize     FB Size.
     * @param derivate   Derivate information.
     * @return The created BoardFamily entity.
     */
    public BoardFamily createFamily(String familyName, String projet, String side,
                                    String fbType2, String fbType3, String fbSize,
                                    String derivate) {
        // Optionally, check for existing familyName to prevent duplicates
        // if (boardFamilyRepository.existsByFamilyName(familyName)) {
        //     throw new IllegalArgumentException("Family name already exists.");
        // }

        BoardFamily family = BoardFamily.builder()
                .familyName(familyName)
                .projet(projet)
                .side(side)
                .fbType2(fbType2)
                .fbType3(fbType3)
                .fbSize(fbSize)
                .derivate(derivate)
                .boardCount(0)
                .build();

        return boardFamilyRepository.save(family);
    }

    /**
     * Retrieves all BoardFamily entities.
     *
     * @return A list of BoardFamily entities.
     */
    public List<BoardFamily> getAllFamilies() {
        return boardFamilyRepository.findAll();
    }

    /**
     * Retrieves a BoardFamily by its ID.
     *
     * @param id The ID of the BoardFamily.
     * @return An Optional containing the BoardFamily if found, else empty.
     */
    public Optional<BoardFamily> getFamilyById(Long id) {
        return boardFamilyRepository.findById(id);
    }

    /**
     * Generates board families for all existing boards.
     *
     * @return The number of families created.
     */
    public int generateFamiliesForExistingBoards() {
        // Implement the logic to generate families based on existing boards
        // This is a placeholder implementation
        // Replace this with actual logic as per your business requirements
        return 0;
    }

    /**
     * Saves BOM lines for a specific BoardFamily.
     *
     * @param family The BoardFamily entity.
     * @param lines  The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @Transactional
    public List<BomLineDTO> saveBOMLines(BoardFamily family, List<BomLineDTO> lines) {
        if (family == null) {
            throw new IllegalArgumentException("BoardFamily cannot be null.");
        }

        // Ensure that the family exists in the repository
        BoardFamily existingFamily = boardFamilyRepository.findById(family.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BoardFamily not found with id: " + family.getId()));

        // Delegate saving BOM lines to BomService
        return bomService.saveBOMLines(existingFamily, lines);
    }

    /**
     * Overloaded method to save BOM lines using family ID.
     *
     * @param familyId The ID of the BoardFamily.
     * @param lines    The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @Transactional
    public List<BomLineDTO> saveBOMLines(Long familyId, List<BomLineDTO> lines) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("BoardFamily not found with id: " + familyId));

        return saveBOMLines(family, lines);
    }

    // Additional business logic methods can be added here
}

