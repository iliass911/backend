package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.service.BoardFamilyService;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.domain.security.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing BoardFamily and related BOM operations.
 */
@RestController
@RequestMapping("/api/board-families")
@CrossOrigin(
    origins = "http://10.150.2.201:3000",
    allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization", "X-Requesting-Module"}
)
public class BoardFamilyController {

    private final BoardFamilyService boardFamilyService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public BoardFamilyController(BoardFamilyService boardFamilyService,
                                 RoleService roleService,
                                 UserService userService) {
        this.boardFamilyService = boardFamilyService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Get all board families.
     * Requires READ permission on the BOM module.
     *
     * @param requestingModule The module requesting this data (from the X-Requesting-Module header).
     * @return A list of BoardFamily entities if permission is granted; otherwise a 403 or error response.
     */
    @GetMapping
    public ResponseEntity<?> getAllFamilies(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    userService.getCurrentUser().getRole(),
                    Module.BOM,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No permission to read board families.");
            }

            List<BoardFamily> families = boardFamilyService.getAllFamilies();
            return ResponseEntity.ok(families);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving board families: " + e.getMessage());
        }
    }

    /**
     * Endpoint to create a new BoardFamily.
     *
     * @param familyRequest The BoardFamily details.
     * @return The created BoardFamily.
     */
    @PostMapping
    public ResponseEntity<?> createFamily(@RequestBody CreateFamilyRequest familyRequest,
                                          @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            // Check permissions for CREATE on BOM
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    userService.getCurrentUser().getRole(),
                    Module.BOM,
                    PermissionType.CREATE,
                    callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No permission to create board families.");
            }

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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating board family: " + e.getMessage());
        }
    }

    /**
     * Endpoint to save BOM lines for a specific BoardFamily.
     *
     * @param familyId The ID of the BoardFamily.
     * @param lines    The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @PostMapping("/{familyId}/bom-lines")
    public ResponseEntity<?> saveBomLines(
            @PathVariable Long familyId,
            @RequestBody List<BomLineDTO> lines,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            // Check permissions for CREATE or UPDATE on BOM
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    userService.getCurrentUser().getRole(),
                    Module.BOM,
                    PermissionType.CREATE,  // or PermissionType.UPDATE depending on your needs
                    callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No permission to save BOM lines.");
            }

            List<BomLineDTO> savedLines = boardFamilyService.saveBOMLines(familyId, lines);
            return ResponseEntity.ok(savedLines);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving BOM lines: " + e.getMessage());
        }
    }

    /**
     * Endpoint to save BOM lines using a BoardFamily object.
     *
     * @param request The request containing BoardFamily and BomLineDTOs.
     * @return The list of saved BomLineDTOs.
     */
    @PostMapping("/save-bom-lines")
    public ResponseEntity<?> saveBomLines(
            @RequestBody SaveBomLinesRequest request,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule
    ) {
        try {
            // Check permissions for CREATE or UPDATE on BOM
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    userService.getCurrentUser().getRole(),
                    Module.BOM,
                    PermissionType.CREATE,  // or PermissionType.UPDATE
                    callingModule
            );

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No permission to save BOM lines.");
            }

            List<BomLineDTO> savedLines = boardFamilyService.saveBOMLines(request.getFamily(), request.getLines());
            return ResponseEntity.ok(savedLines);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving BOM lines: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------------------
    // DTO Classes
    // -----------------------------------------------------------------------------------

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
}
