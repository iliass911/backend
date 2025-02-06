// src/main/java/com/sebn/brettbau/domain/preventive_maintenance/controller/MaintenanceScheduleController.java

package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.MaintenanceScheduleDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.MaintenanceScheduleService;
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
@RequestMapping("/api/maintenance-schedules")
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
public class MaintenanceScheduleController {

    private final MaintenanceScheduleService scheduleService;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public MaintenanceScheduleController(
            MaintenanceScheduleService scheduleService,
            RoleService roleService,
            UserService userService) {
        this.scheduleService = scheduleService;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Get all maintenance schedules.
     * Requires READ permission on the MAINTENANCE_SCHEDULE module.
     *
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the list of MaintenanceScheduleDTOs.
     */
    @GetMapping
    public ResponseEntity<List<MaintenanceScheduleDTO>> getAllSchedules(
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Validate requestingModule if provided
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
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.READ,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.READ,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to read maintenance schedules.");
            }

            List<MaintenanceScheduleDTO> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Assign a pack to a week.
     * Requires CREATE permission on the MAINTENANCE_SCHEDULE module.
     *
     * @param scheduleDTO      The MaintenanceScheduleDTO containing schedule details.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity containing the created MaintenanceScheduleDTO.
     */
    @PostMapping
    public ResponseEntity<MaintenanceScheduleDTO> createSchedule(
            @Valid @RequestBody MaintenanceScheduleDTO scheduleDTO,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Validate requestingModule if provided
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
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.CREATE,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.CREATE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to create maintenance schedules.");
            }

            MaintenanceScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.ok(createdSchedule);

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Unassign a pack from a week using projectId, packId, and weekNumber.
     * Requires DELETE permission on the MAINTENANCE_SCHEDULE module.
     *
     * @param scheduleDTO      The MaintenanceScheduleDTO containing identifiers for unassignment.
     * @param requestingModule Optional header indicating the requesting module.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody MaintenanceScheduleDTO scheduleDTO,
            @RequestHeader(value = "X-Requesting-Module", required = false) String requestingModule) {
        try {
            User currentUser = userService.getCurrentUser();

            // Validate requestingModule if provided
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
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.DELETE,
                        null // No requesting module
                );
            } else {
                hasAccess = roleService.hasPermissionOrIndirectAccess(
                        currentUser.getRole(),
                        Module.MAINTENANCE_SCHEDULE,
                        PermissionType.DELETE,
                        callingModule
                );
            }

            if (!hasAccess) {
                throw new AccessDeniedException("No permission to delete maintenance schedules.");
            }

            scheduleService.deleteSchedule(
                scheduleDTO.getProjectId(), 
                scheduleDTO.getPackId(), 
                scheduleDTO.getWeekNumber()
            );
            return ResponseEntity.ok().build();

        } catch (AccessDeniedException ade) {
            return ResponseEntity.status(403).build();
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            // Log the error and return a generic error response
            return ResponseEntity.status(500).build();
        }
    }
}

