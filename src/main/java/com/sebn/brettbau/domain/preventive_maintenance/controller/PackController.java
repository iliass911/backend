// src/main/java/com/sebn/brettbau/domain/preventive_maintenance/controller/PackController.java
package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.PackDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.PackService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/packs")
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
public class PackController {

    private final PackService packService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public PackController(PackService packService, RoleService roleService, UserService userService) {
        this.packService = packService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PackDTO>> getAllPacks(
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read pack.");
            }

            List<PackDTO> packs = packService.getAllPacks();
            return ResponseEntity.ok(packs);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<PackDTO>> getPacksBySiteId(
            @PathVariable Long siteId,
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read pack.");
            }

            List<PackDTO> packs = packService.getPacksBySiteId(siteId);
            return ResponseEntity.ok(packs);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackDTO> getPackById(
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.READ,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read pack.");
            }

            PackDTO packDTO = packService.getPackById(id);
            return ResponseEntity.ok(packDTO);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<PackDTO> createPack(
            @Valid @RequestBody PackDTO packDTO,
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.CREATE,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.CREATE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create pack.");
            }

            PackDTO createdPack = packService.createPack(packDTO);
            return ResponseEntity.ok(createdPack);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackDTO> updatePack(
            @PathVariable Long id,
            @Valid @RequestBody PackDTO packDTO,
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.UPDATE,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.UPDATE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to update pack.");
            }

            PackDTO updatedPack = packService.updatePack(id, packDTO);
            return ResponseEntity.ok(updatedPack);
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePack(
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

            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.DELETE,
                        null
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.PACK,
                        PermissionType.DELETE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete pack.");
            }

            packService.deletePack(id);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
