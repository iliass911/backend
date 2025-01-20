package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.domain.preventive_maintenance.dto.MaintenanceScheduleDTO;
import com.example.backend.domain.preventive_maintenance.service.MaintenanceScheduleService;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-schedules")
public class MaintenanceScheduleController {

    private final MaintenanceScheduleService scheduleService;

    @Autowired
    public MaintenanceScheduleController(MaintenanceScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // Get all maintenance schedules
    @GetMapping
    public List<MaintenanceScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // Assign a pack to a week
    @PostMapping
    public ResponseEntity<MaintenanceScheduleDTO> createSchedule(@Valid @RequestBody MaintenanceScheduleDTO scheduleDTO) {
        try {
            MaintenanceScheduleDTO createdSchedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.ok(createdSchedule);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Unassign a pack from a week using projectId, packId, and weekNumber
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(@RequestBody MaintenanceScheduleDTO scheduleDTO) {
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
