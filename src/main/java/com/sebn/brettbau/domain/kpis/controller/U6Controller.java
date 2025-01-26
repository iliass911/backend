// src/main/java/com/sebn/brettbau/domain/kpis/controller/U6Controller.java

package com.sebn.brettbau.domain.kpis.controller;

import com.sebn.brettbau.domain.kpis.entity.U6;
import com.sebn.brettbau.domain.kpis.service.U6Service;
import com.sebn.brettbau.domain.role.service.RoleService;
import com.sebn.brettbau.domain.security.Module;
import com.sebn.brettbau.domain.security.PermissionType;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/u6")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class U6Controller {

    private final U6Service u6Service;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public U6Controller(
            U6Service u6Service,
            RoleService roleService,
            UserService userService) {
        this.u6Service = u6Service;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * Create a new U6 record.
     * Example: POST /api/u6
     *
     * @param u6 The U6 entity to create.
     * @return Created U6 record or error message.
     */
    @PostMapping
    public ResponseEntity<U6> create(@RequestBody @Validated U6 u6) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create KPIs");
        }
        U6 savedRecord = u6Service.save(u6);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }

    /**
     * Retrieve all U6 records.
     * Example: GET /api/u6
     *
     * @return List of U6 records or error status.
     */
    @GetMapping
    public ResponseEntity<List<U6>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read KPIs");
        }
        List<U6> records = u6Service.findAll();
        return ResponseEntity.ok(records);
    }

    /**
     * Retrieve a single U6 record by ID.
     * Example: GET /api/u6/{id}
     *
     * @param id The ID of the U6 record.
     * @return U6 record or error status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<U6> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read KPIs");
        }
        Optional<U6> record = u6Service.findById(id);
        return record
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Update an existing U6 record.
     * Example: PUT /api/u6/{id}
     *
     * @param id The ID of the U6 record to update.
     * @param u6 The U6 entity with updated data.
     * @return Updated U6 record or error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<U6> update(@PathVariable Long id, @RequestBody @Validated U6 u6) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.UPDATE)) {
            throw new AccessDeniedException("No permission to update KPIs");
        }
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6.setId(id);
            U6 updatedRecord = u6Service.save(u6);
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Delete a U6 record by ID.
     * Example: DELETE /api/u6/{id}
     *
     * @param id The ID of the U6 record to delete.
     * @return No content status or error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.DELETE)) {
            throw new AccessDeniedException("No permission to delete KPIs");
        }
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6Service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
