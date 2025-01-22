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
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-schedules")
public class MaintenanceScheduleController {

    private final MaintenanceScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    public MaintenanceScheduleController(MaintenanceScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<MaintenanceScheduleDTO> getAllSchedules() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE_SCHEDULE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to view maintenance schedules.");
        }
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    public ResponseEntity<MaintenanceScheduleDTO> createSchedule(@Valid @RequestBody MaintenanceScheduleDTO scheduleDTO) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE_SCHEDULE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create maintenance schedules.");
        }
        try {
            MaintenanceScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.ok(createdSchedule);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(@RequestBody MaintenanceScheduleDTO scheduleDTO) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.MAINTENANCE_SCHEDULE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete maintenance schedules.");
        }
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