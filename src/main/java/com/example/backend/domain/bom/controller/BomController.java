package com.example.backend.domain.bom.controller;

import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.service.BomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
public class BomController {
    private final BomService bomService;

    /**
     * Create a new BOM for a given Board Family.
     * This will create the same BOM for all boards in the family.
     */
    @PostMapping("/family/{familyId}")
    public ResponseEntity<List<BomDTO>> createBomForFamily(@PathVariable Long familyId) {
        List<BomDTO> createdBoms = bomService.createBomForFamily(familyId);
        return ResponseEntity.ok(createdBoms);
    }

    /**
     * Get existing BOMs by Family ID.
     * Returns the BOM template for the family.
     */
    @GetMapping("/family/{familyId}")
    public ResponseEntity<BomDTO> getBomByFamilyId(@PathVariable Long familyId) {
        BomDTO bomDTO = bomService.getBomByFamilyId(familyId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Add or update BOM lines for a family.
     * This will update the BOM for all boards in the family.
     */
    @PostMapping("/family/{familyId}/lines")
    public ResponseEntity<List<BomDTO>> addOrUpdateFamilyBomLines(
            @PathVariable Long familyId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        List<BomDTO> updatedBoms = bomService.addOrUpdateFamilyBomLines(familyId, lineDTOs);
        return ResponseEntity.ok(updatedBoms);
    }

    /**
     * Create a new BOM for a given Board.
     * Kept for backward compatibility and individual board cases.
     */
    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(@PathVariable Long boardId) {
        BomDTO createdBom = bomService.createBom(boardId);
        return ResponseEntity.ok(createdBom);
    }

    /**
     * Get an existing BOM by Board ID.
     * Kept for backward compatibility and individual board cases.
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(@PathVariable Long boardId) {
        BomDTO bomDTO = bomService.getBomByBoardId(boardId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Get an existing BOM by ID.
     * Kept for backward compatibility.
     */
    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(@PathVariable Long bomId) {
        BomDTO bomDTO = bomService.getBom(bomId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Add or update BOM lines for a specific BOM.
     * Kept for backward compatibility and individual board cases.
     */
    @PostMapping("/{bomId}/lines")
    public ResponseEntity<BomDTO> addOrUpdateBomLines(
            @PathVariable Long bomId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        BomDTO updatedBom = bomService.addOrUpdateBomLines(bomId, lineDTOs);
        return ResponseEntity.ok(updatedBom);
    }

    /**
     * Delete a BOM entirely.
     * Kept for backward compatibility.
     */
    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long bomId) {
        bomService.deleteBom(bomId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all BOMs for a family.
     */
    @DeleteMapping("/family/{familyId}")
    public ResponseEntity<Void> deleteFamilyBoms(@PathVariable Long familyId) {
        bomService.deleteFamilyBoms(familyId);
        return ResponseEntity.noContent().build();
    }
}