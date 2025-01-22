package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.BulkBoardRequest;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Get all boards.
     * Example: GET /api/boards
     *
     * @return List of BoardDTOs.
     */
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ boards.");
        }
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
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ boards.");
        }
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
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE boards.");
        }
        try {
            System.out.println("Received board data: " + boardDTO);
            BoardDTO createdBoard = boardService.createBoard(boardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred while creating the board: " + ex.getMessage());
            ex.printStackTrace();
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
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE boards.");
        }
        try {
            BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
            return ResponseEntity.ok(updatedBoard);
        } catch (ResourceNotFoundException ex) {
            System.out.println("Board not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation error during update: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
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
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE boards.");
        }
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

    // ------------------- Distinct Values Endpoints -------------------

    @GetMapping("/projets")
    public ResponseEntity<List<String>> getDistinctProjets() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ projets.");
        }
        try {
            List<String> projets = boardService.getDistinctProjets();
            return ResponseEntity.ok(projets);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct projets: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/plants")
    public ResponseEntity<List<String>> getDistinctPlants() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ plants.");
        }
        try {
            List<String> plants = boardService.getDistinctPlants();
            return ResponseEntity.ok(plants);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct plants: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fbTypes1")
    public ResponseEntity<List<String>> getDistinctFbType1() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ fbTypes1.");
        }
        try {
            List<String> fbTypes1 = boardService.getDistinctFbType1();
            return ResponseEntity.ok(fbTypes1);
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct fbTypes1: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ------------------- Bulk Creation Endpoint -------------------

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkBoards(@Valid @RequestBody BulkBoardRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.BOARD, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE boards in bulk.");
        }
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
}
