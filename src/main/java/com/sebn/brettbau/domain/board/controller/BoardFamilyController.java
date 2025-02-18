package com.sebn.brettbau.domain.board.controller;

import com.sebn.brettbau.domain.board.dto.*;
import com.sebn.brettbau.domain.board.service.BoardFamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/board-families")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class BoardFamilyController {
    private final BoardFamilyService service;

    @GetMapping
    public ResponseEntity<List<BoardFamilyDTO>> getAllBoardFamilies() {
        return ResponseEntity.ok(service.getAllBoardFamilies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardFamilyDTO> getBoardFamilyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBoardFamilyById(id));
    }

    @PostMapping
    public ResponseEntity<BoardFamilyDTO> createBoardFamily(@Valid @RequestBody BoardFamilyDTO dto) {
        return ResponseEntity.ok(service.createBoardFamily(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardFamilyDTO> updateBoardFamily(
            @PathVariable Long id,
            @Valid @RequestBody BoardFamilyDTO dto) {
        return ResponseEntity.ok(service.updateBoardFamily(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardFamily(@PathVariable Long id) {
        service.deleteBoardFamily(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<BoardFamilyDTO> duplicateBoardFamily(
            @PathVariable Long id,
            @RequestParam String newPhase) {
        return ResponseEntity.ok(service.duplicateBoardFamily(id, newPhase));
    }
}