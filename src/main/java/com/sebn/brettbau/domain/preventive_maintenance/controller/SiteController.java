// src/main/java/com/sebn/brettbau/domain/preventive_maintenance/controller/SiteController.java

package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.SiteDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.SiteService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
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
public class SiteController {

    private final SiteService siteService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public SiteController(SiteService siteService, RoleService roleService, UserService userService) {
        this.siteService = siteService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Retrieves all sites.
     * Requires READ permission on the SITE module or indirect access through another permitted module.
     *
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the list of SiteDTOs.
     */
    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Check if requestingModule is valid when provided
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Invalid module name
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            // If no requesting module, assume direct SITE access
            if (callingModule == null) {
                boolean hasDirectAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.READ,
                        null // No requesting module
                );

                if (!hasDirectAccess) {
                    throw new AccessDeniedException("No permission to access site data directly.");
                }
            } else {
                // Check indirect access for other modules
                boolean hasIndirectAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.READ,
                        callingModule
                );

                if (!hasIndirectAccess) {
                    throw new AccessDeniedException(
                            String.format("Module %s has no permission to access site data.", callingModule)
                    );
                }
            }

            List<SiteDTO> sites = siteService.getAllSites();
            return ResponseEntity.ok(sites);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retrieves a specific site by its ID.
     * Requires READ permission on the SITE module or indirect access through another permitted module.
     *
     * @param id               The ID of the site to retrieve.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the SiteDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Check if requestingModule is valid when provided
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            // Check permissions
            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.READ,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.READ,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to access site data.");
            }

            SiteDTO site = siteService.getSiteById(id);
            return ResponseEntity.ok(site);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Creates a new site.
     * Requires CREATE permission on the SITE module.
     *
     * @param dto              The SiteDTO containing site details.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the created SiteDTO.
     */
    @PostMapping
    public ResponseEntity<SiteDTO> createSite(
            @RequestBody SiteDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Check if requestingModule is valid when provided
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            // Check permissions
            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.CREATE,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.CREATE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create site.");
            }

            SiteDTO createdSite = siteService.createSite(dto);
            return ResponseEntity.ok(createdSite);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Updates an existing site by its ID.
     * Requires UPDATE permission on the SITE module.
     *
     * @param id               The ID of the site to update.
     * @param dto              The SiteDTO containing updated site details.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the updated SiteDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(
            @PathVariable Long id,
            @RequestBody SiteDTO dto,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Check if requestingModule is valid when provided
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            // Check permissions
            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.UPDATE,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.UPDATE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to update site.");
            }

            SiteDTO updatedSite = siteService.updateSite(id, dto);
            return ResponseEntity.ok(updatedSite);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Deletes a site by its ID.
     * Requires DELETE permission on the SITE module.
     *
     * @param id               The ID of the site to delete.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(
            @PathVariable Long id,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Check if requestingModule is valid when provided
            Module callingModule = null;
            if (requestingModule != null) {
                try {
                    callingModule = Module.valueOf(requestingModule.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new AccessDeniedException("Invalid requesting module: " + requestingModule);
                }
            }

            // Check permissions
            boolean hasAccess;
            if (callingModule == null) {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.DELETE,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.SITE,
                        PermissionType.DELETE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete site.");
            }

            siteService.deleteSite(id);
            return ResponseEntity.noContent().build();

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).build();
        }
    }

    // Additional endpoints can be added here with appropriate permission checks.
}
