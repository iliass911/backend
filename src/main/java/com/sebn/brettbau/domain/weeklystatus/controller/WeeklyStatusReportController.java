// src/main/java/com/sebn/brettbau/domain/weeklystatus/controller/WeeklyStatusReportController.java

package com.sebn.brettbau.domain.weeklystatus.controller;

import com.sebn.brettbau.domain.weeklystatus.dto.WeeklyStatusReportDTO;
import com.sebn.brettbau.domain.weeklystatus.service.WeeklyStatusReportService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-status")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class WeeklyStatusReportController {
    private final WeeklyStatusReportService service;
    private final RoleService roleService;
    private final UserService userService;

    /**
     * Create a new Weekly Status Report.
     * Example: POST /api/weekly-status
     *
     * @param dto The WeeklyStatusReportDTO containing report details.
     * @return Created WeeklyStatusReportDTO or error message.
     */
    @PostMapping
    public ResponseEntity<WeeklyStatusReportDTO> create(@RequestBody @Validated WeeklyStatusReportDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create Weekly Status Reports");
        }
        WeeklyStatusReportDTO createdReport = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    /**
     * Retrieve all Weekly Status Reports.
     * Example: GET /api/weekly-status
     *
     * @return List of WeeklyStatusReportDTO or error status.
     */
    @GetMapping
    public ResponseEntity<List<WeeklyStatusReportDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read Weekly Status Reports");
        }
        List<WeeklyStatusReportDTO> reports = service.getAll();
        return ResponseEntity.ok(reports);
    }

    /**
     * Retrieve a single Weekly Status Report by ID.
     * Example: GET /api/weekly-status/{id}
     *
     * @param id The ID of the Weekly Status Report.
     * @return WeeklyStatusReportDTO or error status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read Weekly Status Reports");
        }
        WeeklyStatusReportDTO report = service.getById(id);
        if (report == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(report);
    }

    /**
     * Update an existing Weekly Status Report.
     * Example: PUT /api/weekly-status/{id}
     *
     * @param id  The ID of the Weekly Status Report to update.
     * @param dto The WeeklyStatusReportDTO containing updated report details.
     * @return Updated WeeklyStatusReportDTO or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> update(@PathVariable Long id, @RequestBody @Validated WeeklyStatusReportDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update Weekly Status Reports");
        }
        WeeklyStatusReportDTO updatedReport = service.update(id, dto);
        if (updatedReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedReport);
    }

    /**
     * Delete a Weekly Status Report by ID.
     * Example: DELETE /api/weekly-status/{id}
     *
     * @param id The ID of the Weekly Status Report to delete.
     * @return No content status or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete Weekly Status Reports");
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

