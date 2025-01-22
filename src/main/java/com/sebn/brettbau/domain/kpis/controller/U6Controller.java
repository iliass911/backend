package com.sebn.brettbau.domain.kpis.controller;

import com.sebn.brettbau.domain.kpis.entity.U6;
import com.sebn.brettbau.domain.kpis.service.U6Service;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/u6")
public class U6Controller {

    @Autowired
    private U6Service u6Service;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<U6>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ KPIs.");
        }
        List<U6> records = u6Service.findAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<U6> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to READ KPI.");
        }
        Optional<U6> record = u6Service.findById(id);
        return record.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<U6> create(@RequestBody U6 u6) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE KPI.");
        }
        U6 savedRecord = u6Service.save(u6);
        return ResponseEntity.ok(savedRecord);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<U6>> createBulk(@RequestBody List<U6> u6List) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to CREATE KPIs in bulk.");
        }
        List<U6> savedRecords = u6Service.saveAll(u6List);
        return ResponseEntity.ok(savedRecords);
    }

    @PutMapping("/{id}")
    public ResponseEntity<U6> update(@PathVariable Long id, @RequestBody U6 u6) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to UPDATE KPI.");
        }
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6.setId(id);
            U6 updatedRecord = u6Service.save(u6);
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPI, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to DELETE KPI.");
        }
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6Service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
