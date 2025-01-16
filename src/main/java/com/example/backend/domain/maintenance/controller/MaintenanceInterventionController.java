// MaintenanceInterventionController.java
package com.example.backend.domain.maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.example.backend.domain.maintenance.service.MaintenanceInterventionService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@Validated
public class MaintenanceInterventionController extends BaseController {

    private final MaintenanceInterventionService service;

    public MaintenanceInterventionController(PermissionChecker permissionChecker, MaintenanceInterventionService service) {
        super(permissionChecker);
        this.service = service;
    }

    @PostMapping
    public MaintenanceInterventionDTO create(@RequestBody @Validated MaintenanceInterventionDTO dto) {
        checkPermission("MAINTENANCE", "CREATE");
        return service.createIntervention(dto);
    }

    @GetMapping
    public List<MaintenanceInterventionDTO> getAll() {
        checkPermission("MAINTENANCE", "VIEW");
        return service.getAllInterventions();
    }

    @GetMapping("/{id}")
    public MaintenanceInterventionDTO getById(@PathVariable Long id) {
        checkPermission("MAINTENANCE", "VIEW");
        return service.getInterventionById(id);
    }

    @PutMapping("/{id}")  
    public MaintenanceInterventionDTO update(@PathVariable Long id, @RequestBody @Validated MaintenanceInterventionDTO dto) {
        checkPermission("MAINTENANCE", "UPDATE");
        return service.updateIntervention(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        checkPermission("MAINTENANCE", "DELETE");
        service.deleteIntervention(id);
    }
}