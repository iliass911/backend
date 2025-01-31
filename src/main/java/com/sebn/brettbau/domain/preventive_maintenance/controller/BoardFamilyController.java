// src/main/java/com/sebn/brettbau/domain/preventive_maintenance/controller/BoardFamilyController.java

package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-families")
public class BoardFamilyController {

    private final BoardFamilyService boardFamilyService;

    @Autowired
    public BoardFamilyController(BoardFamilyService boardFamilyService) {
        this.boardFamilyService = boardFamilyService;
    }

    /**
     * Endpoint to create a new BoardFamily.
     *
     * @param familyRequest The BoardFamily details.
     * @return The created BoardFamily.
     */
    @PostMapping
    public ResponseEntity<BoardFamily> createFamily(@RequestBody CreateFamilyRequest familyRequest) {
        BoardFamily createdFamily = boardFamilyService.createFamily(
                familyRequest.getFamilyName(),
                familyRequest.getProjet(),
                familyRequest.getSide(),
                familyRequest.getFbType2(),
                familyRequest.getFbType3(),
                familyRequest.getFbSize(),
                familyRequest.getDerivate()
        );
        return ResponseEntity.ok(createdFamily);
    }

    /**
     * Endpoint to save BOM lines for a specific BoardFamily.
     *
     * @param familyId The ID of the BoardFamily.
     * @param lines    The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @PostMapping("/{familyId}/bom-lines")
    public ResponseEntity<List<BomLineDTO>> saveBomLines(
            @PathVariable Long familyId,
            @RequestBody List<BomLineDTO> lines) {
        List<BomLineDTO> savedLines = boardFamilyService.saveBOMLines(familyId, lines);
        return ResponseEntity.ok(savedLines);
    }

    /**
     * Endpoint to save BOM lines using a BoardFamily object.
     *
     * @param request The request containing BoardFamily and BomLineDTOs.
     * @return The list of saved BomLineDTOs.
     */
    @PostMapping("/save-bom-lines")
    public ResponseEntity<List<BomLineDTO>> saveBomLines(
            @RequestBody SaveBomLinesRequest request) {
        List<BomLineDTO> savedLines = boardFamilyService.saveBOMLines(request.getFamily(), request.getLines());
        return ResponseEntity.ok(savedLines);
    }

    // DTO Classes

    /**
     * DTO for creating a new BoardFamily.
     */
    public static class CreateFamilyRequest {
        private String familyName;
        private String projet;
        private String side;
        private String fbType2;
        private String fbType3;
        private String fbSize;
        private String derivate;

        // Getters and Setters

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getProjet() {
            return projet;
        }

        public void setProjet(String projet) {
            this.projet = projet;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public String getFbType2() {
            return fbType2;
        }

        public void setFbType2(String fbType2) {
            this.fbType2 = fbType2;
        }

        public String getFbType3() {
            return fbType3;
        }

        public void setFbType3(String fbType3) {
            this.fbType3 = fbType3;
        }

        public String getFbSize() {
            return fbSize;
        }

        public void setFbSize(String fbSize) {
            this.fbSize = fbSize;
        }

        public String getDerivate() {
            return derivate;
        }

        public void setDerivate(String derivate) {
            this.derivate = derivate;
        }
    }

    /**
     * DTO for saving BOM lines using a BoardFamily object.
     */
    public static class SaveBomLinesRequest {
        private BoardFamily family;
        private List<BomLineDTO> lines;

        // Getters and Setters

        public BoardFamily getFamily() {
            return family;
        }

        public void setFamily(BoardFamily family) {
            this.family = family;
        }

        public List<BomLineDTO> getLines() {
            return lines;
        }

        public void setLines(List<BomLineDTO> lines) {
            this.lines = lines;
        }
    }

    // Additional endpoints can be added here as needed
}

