// MaintenanceScheduleController.java  
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.MaintenanceScheduleDTO;
import com.example.backend.domain.preventive_maintenance.service.MaintenanceScheduleService;
import com.example.backend.domain.role.service.PermissionChecker;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-schedules")
public class MaintenanceScheduleController extends BaseController {

    private final MaintenanceScheduleService scheduleService;

    public MaintenanceScheduleController(PermissionChecker permissionChecker, MaintenanceScheduleService scheduleService) {
        super(permissionChecker);
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<MaintenanceScheduleDTO> getAllSchedules() {
        checkPermission("MAINTENANCE_SCHEDULE", "VIEW");
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    public ResponseEntity<MaintenanceScheduleDTO> createSchedule(@Valid @RequestBody MaintenanceScheduleDTO scheduleDTO) {
        checkPermission("MAINTENANCE_SCHEDULE", "CREATE");
        try {
            MaintenanceScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.ok(createdSchedule);  
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(@RequestBody MaintenanceScheduleDTO scheduleDTO) {
        checkPermission("MAINTENANCE_SCHEDULE", "DELETE");
        try {
            scheduleService.deleteSchedule(
                scheduleDTO.getProjectId(), 
                scheduleDTO.getPackId(), 
                scheduleDTO.getWeekNumber()
            );
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}