package com.sebn.brettbau.domain.preventive_maintenance.controller;

import com.sebn.brettbau.domain.preventive_maintenance.dto.SiteDTO;
import com.sebn.brettbau.domain.preventive_maintenance.service.SiteService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;
    private final UserService userService;
    private final RoleService roleService;

    public SiteController(SiteService siteService, UserService userService, RoleService roleService) {
        this.siteService = siteService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.SITE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ sites.");
        }
        return ResponseEntity.ok(siteService.getAllSites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.SITE, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ site details.");
        }
        return ResponseEntity.ok(siteService.getSiteById(id));
    }

    @PostMapping
    public ResponseEntity<SiteDTO> createSite(@RequestBody SiteDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.SITE, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE sites.");
        }
        return ResponseEntity.ok(siteService.createSite(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(@PathVariable Long id, @RequestBody SiteDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.SITE, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE site details.");
        }
        return ResponseEntity.ok(siteService.updateSite(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.SITE, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE sites.");
        }
        siteService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }
}
