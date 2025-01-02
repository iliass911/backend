// BomController.java
package com.example.backend.domain.bom.controller;

import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.service.BomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/boms")
@RequiredArgsConstructor
public class BomController {
    private final BomService bomService;

    /**
     * Create a new BOM for a given Board.
     */
    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(@PathVariable Long boardId) {
        BomDTO createdBom = bomService.createBom(boardId);
        return ResponseEntity.ok(createdBom);
    }

    /**
     * Get an existing BOM by Board ID.
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(@PathVariable Long boardId) {
        BomDTO bomDTO = bomService.getBomByBoardId(boardId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Get an existing BOM by ID.
     */
    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(@PathVariable Long bomId) {
        BomDTO bomDTO = bomService.getBom(bomId);
        return ResponseEntity.ok(bomDTO);
    }

    /**
     * Add or update BOM lines.
     */
    @PostMapping("/{bomId}/lines")
    public ResponseEntity<BomDTO> addOrUpdateBomLines(@PathVariable Long bomId, @RequestBody Set<BomLineDTO> lineDTOs) {
        BomDTO updatedBom = bomService.addOrUpdateBomLines(bomId, lineDTOs);
        return ResponseEntity.ok(updatedBom);
    }

    /**
     * Delete a BOM entirely.
     */
    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long bomId) {
        bomService.deleteBom(bomId);
        return ResponseEntity.noContent().build();
    }
}