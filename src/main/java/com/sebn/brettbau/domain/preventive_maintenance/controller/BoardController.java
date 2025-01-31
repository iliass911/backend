package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.BulkBoardRequest;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing Board entities with security permissions.
 */
@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {
    "Origin", "Content-Type", "Accept", "Authorization", "X-Requesting-Module"
})
public class BoardController {

    private final BoardService boardService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public BoardController(BoardService boardService, RoleService roleService, UserService userService) {
        this.boardService = boardService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Get all boards.
     * Example: GET /api/boards
     *
     * @param requestingModule Optional header to specify the requesting module.
     * @return List of BoardDTOs or appropriate error status.
     */
    @GetMapping
    public ResponseEntity<?> getAllBoards(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = requestingModule != null ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<BoardDTO> boards = boardService.getAllBoards();
            return ResponseEntity.ok(boards);
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving boards.");
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
            boolean hasPermission = roleService.roleHasPermission(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.CREATE
            );

            if (!hasPermission) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

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
     * Get board by ID.
     * Example: GET /api/boards/{id}
     *
     * @param id               The ID of the board.
     * @param requestingModule Optional header to specify the requesting module.
     * @return ResponseEntity containing BoardDTO or error status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = requestingModule != null ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            BoardDTO boardDTO = boardService.getBoardById(id);
            return ResponseEntity.ok(boardDTO);
        } catch (ResourceNotFoundException ex) {
            System.out.println("Board not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving the board.");
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
            boolean hasPermission = roleService.roleHasPermission(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.UPDATE
            );

            if (!hasPermission) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

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
            boolean hasPermission = roleService.roleHasPermission(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.DELETE
            );

            if (!hasPermission) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

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
     * @param requestingModule Optional header to specify the requesting module.
     * @return List of distinct projets or error status.
     */
    @GetMapping("/projets")
    public ResponseEntity<?> getDistinctProjets(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = requestingModule != null ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<String> projets = boardService.getDistinctProjets();
            return ResponseEntity.ok(projets);
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct projets: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve distinct projets.");
        }
    }

    /**
     * Get all distinct plants.
     * Example: GET /api/boards/plants
     *
     * @param requestingModule Optional header to specify the requesting module.
     * @return List of distinct plants or error status.
     */
    @GetMapping("/plants")
    public ResponseEntity<?> getDistinctPlants(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = requestingModule != null ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<String> plants = boardService.getDistinctPlants();
            return ResponseEntity.ok(plants);
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
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
     * @param requestingModule Optional header to specify the requesting module.
     * @return List of distinct fbType1 values or error status.
     */
    @GetMapping("/fbTypes1")
    public ResponseEntity<?> getDistinctFbType1(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = requestingModule != null ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<String> fbTypes1 = boardService.getDistinctFbType1();
            return ResponseEntity.ok(fbTypes1);
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            System.out.println("An error occurred while fetching distinct fbTypes1: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve distinct fbTypes1.");
        }
    }

    // ------------------- New Bulk Creation Endpoint -------------------

    /**
     * Create multiple boards in bulk.
     * Example: POST /api/boards/bulk
     *
     * @param request The BulkBoardRequest containing a list of BoardDTOs.
     * @return ResponseEntity containing list of created BoardDTOs or error message.
     */
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkBoards(
            @Valid @RequestBody BulkBoardRequest request,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            boolean hasPermission = roleService.roleHasPermission(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.CREATE
            );

            if (!hasPermission) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

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

