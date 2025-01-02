package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.service.BoardService;
import com.example.backend.exception.ResourceNotFoundException; // Ensure this import exists
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Ensure this import exists

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // Get all boards
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    // Get board by ID
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        try {
            BoardDTO boardDTO = boardService.getBoardById(id);
            return ResponseEntity.ok(boardDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create new board
    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(@Valid @RequestBody BoardDTO boardDTO) { // Added @Valid
        BoardDTO createdBoard = boardService.createBoard(boardDTO);
        return ResponseEntity.ok(createdBoard);
    }

    // Update existing board
    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardDTO boardDTO) { // Added @Valid
        try {
            BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
            return ResponseEntity.ok(updatedBoard);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete board
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------- New Endpoints for Distinct Values -------------------

    /**
     * Get all distinct projets.
     * Example: GET /api/boards/projets
     */
    @GetMapping("/projets")
    public ResponseEntity<List<String>> getDistinctProjets() {
        List<String> projets = boardService.getDistinctProjets();
        return ResponseEntity.ok(projets);
    }

    /**
     * Get all distinct plants.
     * Example: GET /api/boards/plants
     */
    @GetMapping("/plants")
    public ResponseEntity<List<String>> getDistinctPlants() {
        List<String> plants = boardService.getDistinctPlants();
        return ResponseEntity.ok(plants);
    }

    /**
     * Get all distinct fbType1 values.
     * Example: GET /api/boards/fbTypes1
     */
    @GetMapping("/fbTypes1")
    public ResponseEntity<List<String>> getDistinctFbType1() {
        List<String> fbTypes1 = boardService.getDistinctFbType1();
        return ResponseEntity.ok(fbTypes1);
    }

    // ---------------------------------------------------------------------------
}
