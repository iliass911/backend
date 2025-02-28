package com.sebn.brettbau.domain.movement.controller;

import com.sebn.brettbau.domain.movement.dto.BoardMovementDTO;
import com.sebn.brettbau.domain.movement.service.BoardMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board-movements")
@RequiredArgsConstructor
public class BoardMovementController {

    private final BoardMovementService service;

    @GetMapping
    public List<BoardMovementDTO> getAllMovements() {
        return service.getAllMovements();
    }

    @GetMapping("/{id}")
    public BoardMovementDTO getMovement(@PathVariable Long id) {
        return service.getMovementById(id);
    }

    @PostMapping
    public ResponseEntity<BoardMovementDTO> createMovement(@RequestBody BoardMovementDTO dto) {
        BoardMovementDTO saved = service.createOrUpdate(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardMovementDTO> updateMovement(@PathVariable Long id, @RequestBody BoardMovementDTO dto) {
        dto.setId(id);
        BoardMovementDTO updated = service.createOrUpdate(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        service.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
