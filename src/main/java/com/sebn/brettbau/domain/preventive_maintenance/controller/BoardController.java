package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.BulkBoardRequest;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Pack;
import com.sebn.brettbau.domain.preventive_maintenance.repository.PackRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing Board entities with security permissions.
 */
@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "http://10.150.2.201:3000", allowedHeaders = {
    "Origin", "Content-Type", "Accept", "Authorization", "X-Requesting-Module"
})
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;
    private final RoleService roleService;
    private final UserService userService;
    private final PackRepository packRepository;

    @Autowired
    public BoardController(BoardService boardService,
                           RoleService roleService,
                           UserService userService,
                           PackRepository packRepository) {
        this.boardService = boardService;
        this.roleService = roleService;
        this.userService = userService;
        this.packRepository = packRepository;
    }

    /**
     * Get all boards with optional filtering via query params.
     * Example: GET /api/boards?search=xyz&projet=abc&plant=XXX&fbType1=YYY
     *
     * @param requestingModule Optional header to specify the requesting module.
     * @param search           (optional) Search term for FB Name or Board Number
     * @param projet           (optional) Filter by projet
     * @param plant            (optional) Filter by plant
     * @param fbType1          (optional) Filter by FB Type 1
     * @return List of BoardDTOs
     */
    @GetMapping
    public ResponseEntity<?> getAllBoards(
        @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "projet", required = false) String projet,
        @RequestParam(value = "plant", required = false) String plant,
        @RequestParam(value = "fbType1", required = false) String fbType1
    ) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<BoardDTO> boards = boardService.getBoardsFiltered(search, projet, plant, fbType1);
            return ResponseEntity.ok(boards);
        } catch (IllegalArgumentException ex) {
            // Handle invalid module name in header
            logger.error("Invalid requesting module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error retrieving boards: {}", ex.getMessage(), ex);
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

            // Optional uniqueness check for boardNumber, if desired:
            // if (boardService.existsBoardNumber(boardDTO.getBoardNumber())) {
            //     return ResponseEntity.badRequest().body("Board Number already exists.");
            // }

            BoardDTO createdBoard = boardService.createBoard(boardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        } catch (IllegalArgumentException ex) {
            logger.error("Validation error in createBoard: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error creating board: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the board.");
        }
    }

    /**
     * Get board by ID.
     * Example: GET /api/boards/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardById(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

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
            logger.warn("Board not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid requesting module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error retrieving board: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving the board.");
        }
    }

    /**
     * Update existing board.
     * Example: PUT /api/boards/{id}
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
            logger.warn("Board not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (IllegalArgumentException ex) {
            logger.error("Validation error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error updating board: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the board.");
        }
    }

    /**
     * Delete board.
     * Example: DELETE /api/boards/{id}
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
            logger.info("Successfully deleted board with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.warn("Board not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found.");
        } catch (Exception ex) {
            logger.error("Error deleting board: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the board.");
        }
    }

    // ------------------- Updated Endpoints for Distinct Values -------------------

    /**
     * Get all distinct projects.
     * Example: GET /api/boards/projects
     */
    @GetMapping("/projects")
    public ResponseEntity<?> getDistinctProjects(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );
            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            // "projets" -> "projects"
            List<String> projects = boardService.getDistinctProjets();
            return ResponseEntity.ok(projects);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid requesting module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error fetching distinct projects: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve distinct projects.");
        }
    }

    /**
     * Get all distinct sites (renamed from "plants").
     * Example: GET /api/boards/sites
     */
    @GetMapping("/sites")
    public ResponseEntity<?> getDistinctSites(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );
            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            // This calls the old getDistinctPlants() but we can rename it in the service
            List<String> sites = boardService.getDistinctPlants();
            return ResponseEntity.ok(sites);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid requesting module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error fetching distinct sites: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve distinct sites.");
        }
    }

    /**
     * Get all distinct fbType1 values.
     * Example: GET /api/boards/fbTypes1
     */
    @GetMapping("/fbTypes1")
    public ResponseEntity<?> getDistinctFbType1(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

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
            logger.error("Invalid requesting module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error fetching distinct fbTypes1: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve distinct fbTypes1.");
        }
    }

    // ------------------- New Bulk Creation Endpoint -------------------

    /**
     * Create multiple boards in bulk.
     * Example: POST /api/boards/bulk
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
            logger.error("Bulk creation error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error during bulk creation: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during bulk board creation.");
        }
    }

    // ------------------- Packs Endpoint (Example) -------------------

    /**
     * Get all packs. Just an example if the frontend needs it.
     * Example: GET /api/boards/packs
     */
    @GetMapping("/packs")
    public ResponseEntity<?> getAllPacks(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            Module callingModule = (requestingModule != null) ? Module.valueOf(requestingModule.toUpperCase()) : null;

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                userService.getCurrentUser().getRole(),
                Module.BOARD,
                PermissionType.READ,
                callingModule
            );
            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
            }

            List<Pack> packs = packRepository.findAll();
            return ResponseEntity.ok(packs);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid module: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid requesting module.");
        } catch (Exception ex) {
            logger.error("Error fetching packs: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve packs.");
        }
    }
}
