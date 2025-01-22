package com.sebn.brettbau.domain.weeklystatus.controller;

import com.sebn.brettbau.domain.weeklystatus.dto.WeeklyStatusReportDTO;
import com.sebn.brettbau.domain.weeklystatus.service.WeeklyStatusReportService;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-status")
@RequiredArgsConstructor
public class WeeklyStatusReportController {
    private final WeeklyStatusReportService service;
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<WeeklyStatusReportDTO>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ weekly reports.");
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ weekly reports.");
        }
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<WeeklyStatusReportDTO> create(@RequestBody WeeklyStatusReportDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE weekly reports.");
        }
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> update(@PathVariable Long id, @RequestBody WeeklyStatusReportDTO dto) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE weekly reports.");
        }
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.WEEKLY_REPORT, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE weekly reports.");
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
