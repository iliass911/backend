// src/main/java/com/sebn/brettbau/domain/bom/controller/BomController.java

package com.sebn.brettbau.domain.bom.controller;

import com.sebn.brettbau.domain.bom.dto.BomDTO;
import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.bom.service.BomService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/boms")
@CrossOrigin(
    origins = "http://localhost:3000",
    allowedHeaders = {
        "Origin", 
        "Content-Type", 
        "Accept", 
        "Authorization",
        "X-Requesting-Module"
    }
)
@Validated
public class BomController {

    private final BomService bomService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public BomController(BomService bomService, RoleService roleService, UserService userService) {
        this.bomService = bomService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Create a new BOM for a given Board Family.
     * This will create the same BOM for all boards in the family.
     * Requires CREATE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @PostMapping("/family/{familyId}")
    public ResponseEntity<List<BomDTO>> createBomForFamily(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long familyId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.CREATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create BOMs.");
            }

            List<BomDTO> createdBoms = bomService.createBomForFamily(familyId);
            return ResponseEntity.ok(createdBoms);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get existing BOMs by Family ID.
     * Returns the BOM template for the family.
     * Requires READ permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @GetMapping("/family/{familyId}")
    public ResponseEntity<BomDTO> getBomByFamilyId(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long familyId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read BOMs.");
            }

            BomDTO bomDTO = bomService.getBomByFamilyId(familyId);
            return ResponseEntity.ok(bomDTO);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Add or update BOM lines for a family.
     * This will update the BOM for all boards in the family.
     * Requires UPDATE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @PostMapping("/family/{familyId}/lines")
    public ResponseEntity<List<BomDTO>> addOrUpdateFamilyBomLines(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long familyId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.UPDATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to update BOMs.");
            }

            List<BomDTO> updatedBoms = bomService.addOrUpdateFamilyBomLines(familyId, lineDTOs);
            return ResponseEntity.ok(updatedBoms);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Create a new BOM for a given Board.
     * Kept for backward compatibility and individual board cases.
     * Requires CREATE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @PostMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> createBomForBoard(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long boardId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.CREATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create BOMs.");
            }

            BomDTO createdBom = bomService.createBom(boardId);
            return ResponseEntity.ok(createdBom);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get an existing BOM by Board ID.
     * Kept for backward compatibility and individual board cases.
     * Requires READ permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BomDTO> getBomByBoardId(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long boardId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read BOMs.");
            }

            BomDTO bomDTO = bomService.getBomByBoardId(boardId);
            return ResponseEntity.ok(bomDTO);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get an existing BOM by ID.
     * Kept for backward compatibility.
     * Requires READ permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @GetMapping("/{bomId}")
    public ResponseEntity<BomDTO> getBom(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long bomId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read BOMs.");
            }

            BomDTO bomDTO = bomService.getBom(bomId);
            return ResponseEntity.ok(bomDTO);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Add or update BOM lines for a specific BOM.
     * Kept for backward compatibility and individual board cases.
     * Requires UPDATE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @PostMapping("/{bomId}/lines")
    public ResponseEntity<BomDTO> addOrUpdateBomLines(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long bomId,
            @RequestBody Set<BomLineDTO> lineDTOs) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.UPDATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to update BOMs.");
            }

            BomDTO updatedBom = bomService.addOrUpdateBomLines(bomId, lineDTOs);
            return ResponseEntity.ok(updatedBom);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete a BOM entirely.
     * Kept for backward compatibility.
     * Requires DELETE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @DeleteMapping("/{bomId}")
    public ResponseEntity<Void> deleteBom(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long bomId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.DELETE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete BOMs.");
            }

            bomService.deleteBom(bomId);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete all BOMs for a family.
     * Requires DELETE permission on the BOM module or indirect access through BOARD, SITE, PROJECT, INVENTORY modules.
     */
    @DeleteMapping("/family/{familyId}")
    public ResponseEntity<Void> deleteFamilyBoms(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule,
            @PathVariable Long familyId) {
        try {
            User currentUser = userService.getCurrentUser();

            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            boolean hasAccess = roleService.hasPermissionOrIndirectAccess(
                    currentUser.getRole(),
                    Module.BOM,
                    PermissionType.DELETE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete BOMs.");
            }

            bomService.deleteFamilyBoms(familyId);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error (consider using a logger)
            return ResponseEntity.status(500).build();
        }
    }
}

