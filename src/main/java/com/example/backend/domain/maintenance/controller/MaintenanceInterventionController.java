package com.example.backend.domain.maintenance.controller;

import com.example.backend.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.example.backend.domain.maintenance.service.MaintenanceInterventionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@Validated
public class MaintenanceInterventionController {

    private final MaintenanceInterventionService service;

    public MaintenanceInterventionController(MaintenanceInterventionService service) {
        this.service = service;
    }

    @PostMapping
    public MaintenanceInterventionDTO create(@RequestBody @Validated MaintenanceInterventionDTO dto) {
        return service.createIntervention(dto);
    }

    @GetMapping
    public List<MaintenanceInterventionDTO> getAll() {
        return service.getAllInterventions();
    }

    @GetMapping("/{id}")
    public MaintenanceInterventionDTO getById(@PathVariable Long id) {
        return service.getInterventionById(id);
    }

    @PutMapping("/{id}")
    public MaintenanceInterventionDTO update(@PathVariable Long id, @RequestBody @Validated MaintenanceInterventionDTO dto) {
        return service.updateIntervention(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteIntervention(id);
    }
}
