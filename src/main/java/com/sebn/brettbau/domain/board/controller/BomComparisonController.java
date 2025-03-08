package com.sebn.brettbau.domain.board.controller;

import com.sebn.brettbau.domain.board.dto.BomComparisonItemDTO;
import com.sebn.brettbau.domain.board.service.BomComparisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-families")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BomComparisonController {

    private final BomComparisonService comparisonService;

    public BomComparisonController(BomComparisonService comparisonService) {
         this.comparisonService = comparisonService;
    }

    @GetMapping("/compare")
    public ResponseEntity<List<BomComparisonItemDTO>> compareFamilies(@RequestParam Long family1Id,
                                                                       @RequestParam Long family2Id) {
         List<BomComparisonItemDTO> comparisons = comparisonService.compareBomItems(family1Id, family2Id);
         return ResponseEntity.ok(comparisons);
    }
}