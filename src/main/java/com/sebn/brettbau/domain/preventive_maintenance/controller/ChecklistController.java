// src/main/java/com/example/backend/domain/preventive_maintenance/controller/ChecklistController.java

package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ChecklistDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.ProgressResponse;
import com.sebn.brettbau.domain.preventive_maintenance.service.ChecklistService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/checklists")
@CrossOrigin(
    origins = "http://10.150.2.201:3000",
    allowedHeaders = {
        "Origin", 
        "Content-Type", 
        "Accept", 
        "Authorization",
        "X-Requesting-Module"
    }
)
@Validated
public class ChecklistController {
    private final ChecklistService checklistService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public ChecklistController(ChecklistService checklistService, RoleService roleService, UserService userService) {
        this.checklistService = checklistService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Get all checklists.
     * Requires READ permission on the USERS_PREVENTIVE module.
     */
    @GetMapping
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read checklists.");
            }

            List<ChecklistDTO> checklists = checklistService.getAllChecklists();
            return ResponseEntity.ok(checklists);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get a specific checklist by ID.
     * Requires READ permission on the USERS_PREVENTIVE module.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklist(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read checklists.");
            }

            ChecklistDTO checklist = checklistService.getChecklistById(id);
            return ResponseEntity.ok(checklist);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Create a new checklist.
     * Requires CREATE permission on the USERS_PREVENTIVE module.
     */
    @PostMapping
    public ResponseEntity<ChecklistDTO> createChecklist(
            @Valid @RequestBody ChecklistDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.CREATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create checklists.");
            }

            ChecklistDTO createdChecklist = checklistService.createChecklist(dto);
            return ResponseEntity.ok(createdChecklist);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Update an existing checklist.
     * Requires UPDATE permission on the USERS_PREVENTIVE module.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChecklistDTO> updateChecklist(
            @PathVariable Long id,
            @Valid @RequestBody ChecklistDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.UPDATE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to update checklists.");
            }

            ChecklistDTO updatedChecklist = checklistService.updateChecklist(id, dto);
            return ResponseEntity.ok(updatedChecklist);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete a checklist by ID.
     * Requires DELETE permission on the USERS_PREVENTIVE module.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChecklist(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.DELETE,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete checklists.");
            }

            checklistService.deleteChecklist(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get user progress.
     * Requires READ permission on the USERS_PREVENTIVE module.
     */
    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ProgressResponse> getUserProgress(
            @PathVariable Long userId,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
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
                    Module.USERS_PREVENTIVE,
                    PermissionType.READ,
                    callingModule
            );

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to view user progress.");
            }

            ProgressResponse progress = checklistService.getUserProgress(userId);
            if (progress != null) {
                return ResponseEntity.ok(progress);
            }
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

