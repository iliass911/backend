package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.dto.BulkBoardRequest; // Ensure this import exists
import com.example.backend.domain.preventive_maintenance.service.BoardService;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing Board entities.
 */
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    /**
     * Get all boards.
     * Example: GET /api/boards
     *
     * @return List of BoardDTOs.
     */
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    /**
     * Get board by ID.
     * Example: GET /api/boards/{id}
     *
     * @param id The ID of the board.
     * @return ResponseEntity containing BoardDTO or error status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        try {
            BoardDTO boardDTO = boardService.getBoardById(id);
            return ResponseEntity.ok(boardDTO);
        } catch (ResourceNotFoundException ex) {
            System.out.println("Board not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            System.out.println("Unexpected error while retrieving board with id " + id + ": " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Create new board.
     * Example: POST /api/boards
     *
     * @param boardDTO The BoardDTO containing board details.
     * @return ResponseEntity containing created BoardDTO or error message.
     */
    @PostMapping
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardDTO boardDTO) {
        try {
            System.out.println("Received board data: " + boardDTO);
            BoardDTO createdBoard = boardService.createBoard(boardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (IllegalArgumentException ex) {
            // Return a bad request with the error message
            System.out.println("Validation error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            // Handle other exceptions
            System.out.println("An error occurred while creating the board: " + ex.getMessage());
            ex.printStackTrace(); // Log full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the board.");
        }
    }

    /**
     * Update existing board.
     * Example: PUT /api/boards/{id}
     *
     * @param id       The ID of the board to update.
     * @param boardDTO The BoardDTO containing updated board details.
     * @return ResponseEntity containing updated BoardDTO or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardDTO boardDTO) {
        try {
            BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
            return ResponseEntity.ok(updatedBoard);
        } catch (ResourceNotFoundException ex) {
            System.out.println("Board not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (IllegalArgumentException ex) {
            // Return a bad request with the error message
            System.out.println("Validation error during update: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            // Handle other exceptions
            System.out.println("An error occurred while updating the board: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the board.");
        }
    }

    /**
     * Delete board.
     * Example: DELETE /api/boards/{id}
     *
     * @param id The ID of the board to delete.
     * @return ResponseEntity with appropriate status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        try {
            boardService.deleteBoard(id);
            System.out.println("Successfully deleted board with id: " + id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            System.out.println("Board not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (Exception ex) {
            System.out.println("An error occurred while deleting the board: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the board.");
        }
    }

    // ------------------- New Endpoints for Distinct Values -------------------

    /**
     * Get all distinct projets.
     * Example: GET /api/boards/projets
     *
     * @return List of distinct projets.
     */
    @GetMapping("/projets")
    public ResponseEntity<List<String>> getDistinctProjets() {
        try {
            List<String> projets = boardService.getDistinctProjets();
            return ResponseEntity.ok(projets);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct projets: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all distinct plants.
     * Example: GET /api/boards/plants
     *
     * @return List of distinct plants.
     */
    @GetMapping("/plants")
    public ResponseEntity<List<String>> getDistinctPlants() {
        try {
            List<String> plants = boardService.getDistinctPlants();
            return ResponseEntity.ok(plants);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct plants: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all distinct fbType1 values.
     * Example: GET /api/boards/fbTypes1
     *
     * @return List of distinct fbType1 values.
     */
    @GetMapping("/fbTypes1")
    public ResponseEntity<List<String>> getDistinctFbType1() {
        try {
            List<String> fbTypes1 = boardService.getDistinctFbType1();
            return ResponseEntity.ok(fbTypes1);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct fbTypes1: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ---------------------------------------------------------------------------

    // ------------------- New Bulk Creation Endpoint -------------------

    /**
     * Create multiple boards in bulk.
     * Example: POST /api/boards/bulk
     *
     * @param request The BulkBoardRequest containing a list of BoardDTOs.
     * @return ResponseEntity containing list of created BoardDTOs or error message.
     */
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkBoards(@Valid @RequestBody BulkBoardRequest request) {
        try {
            List<BoardDTO> createdBoards = boardService.createBulkBoards(request.getBoards());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoards);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred during bulk board creation: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during bulk board creation.");
        }
    }

    // ---------------------------------------------------------------------------
}
