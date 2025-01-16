// SiteController.java
package com.example.backend.domain.preventive_maintenance.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.preventive_maintenance.dto.SiteDTO;
import com.example.backend.domain.preventive_maintenance.service.SiteService;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController extends BaseController {

    private final SiteService siteService;

    public SiteController(PermissionChecker permissionChecker, SiteService siteService) {
        super(permissionChecker);
        this.siteService = siteService;
    }

    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        checkPermission("SITE", "VIEW");
        return ResponseEntity.ok(siteService.getAllSites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
        checkPermission("SITE", "VIEW");
        return ResponseEntity.ok(siteService.getSiteById(id));
    }

    @PostMapping
    public ResponseEntity<SiteDTO> createSite(@RequestBody SiteDTO dto) {
        checkPermission("SITE", "CREATE");
        return ResponseEntity.ok(siteService.createSite(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(@PathVariable Long id, @RequestBody SiteDTO dto) {
        checkPermission("SITE", "UPDATE");
        return ResponseEntity.ok(siteService.updateSite(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        checkPermission("SITE", "DELETE");
        siteService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }
}