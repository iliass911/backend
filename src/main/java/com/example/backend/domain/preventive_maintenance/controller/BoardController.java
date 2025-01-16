// BoardController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.dto.BulkBoardRequest;
import com.example.backend.domain.preventive_maintenance.service.BoardService;
import com.example.backend.domain.role.service.PermissionChecker;
import com.example.backend.exception.ResourceNotFoundException;  // Import custom exception
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController extends BaseController {

    private final BoardService boardService;

    public BoardController(PermissionChecker permissionChecker, BoardService boardService) {
        super(permissionChecker);
        this.boardService = boardService;
    }

    @GetMapping
    public List<BoardDTO> getAllBoards() {
        checkPermission("BOARD", "VIEW");
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable Long id) {
        checkPermission("BOARD", "VIEW");
        try {
            BoardDTO boardDTO = boardService.getBoardById(id);
            return ResponseEntity.ok(boardDTO);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBoard(@Valid @RequestBody BoardDTO boardDTO) {
        checkPermission("BOARD", "CREATE");
        try {
            BoardDTO createdBoard = boardService.createBoard(boardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardDTO boardDTO) {
        checkPermission("BOARD", "UPDATE");
        try {
            BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
            return ResponseEntity.ok(updatedBoard);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        checkPermission("BOARD", "DELETE");
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @GetMapping("/projets")
    public ResponseEntity<?> getDistinctProjets() {
        checkPermission("BOARD", "VIEW");
        try {
            List<String> projets = boardService.getDistinctProjets();
            return ResponseEntity.ok(projets);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @GetMapping("/plants")
    public ResponseEntity<?> getDistinctPlants() {
        checkPermission("BOARD", "VIEW");
        try {
            List<String> plants = boardService.getDistinctPlants();
            return ResponseEntity.ok(plants);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @GetMapping("/fbTypes1")
    public ResponseEntity<?> getDistinctFbType1() {
        checkPermission("BOARD", "VIEW");
        try {
            List<String> fbTypes1 = boardService.getDistinctFbType1();
            return ResponseEntity.ok(fbTypes1);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkBoards(@Valid @RequestBody BulkBoardRequest request) {
        checkPermission("BOARD", "CREATE");
        try {
            List<BoardDTO> createdBoards = boardService.createBulkBoards(request.getBoards());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoards);
        } catch (Exception ex) {
            return handleException(ex);
        }
    }

    private ResponseEntity<?> handleException(Exception ex) {
        if (ex instanceof ResourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } else if (ex instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
