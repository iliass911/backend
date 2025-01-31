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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
            UserService userService
    ) {
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

    /**
     * Bulk create U6 records (with detailed logging).
     * Example: POST /api/u6/bulk
     *
     * @param records List of Maps representing U6 fields.
     * @return Created U6 records or error message.
     */
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody List<Map<String, Object>> records) {
        // If you still want role checks for bulk creation, re-add them here:
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create KPIs");
        }

        try {
            // 1. Log incoming data
            System.out.println("Received " + records.size() + " records");

            List<U6> u6Records = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE;

            for (int i = 0; i < records.size(); i++) {
                try {
                    Map<String, Object> record = records.get(i);
                    System.out.println("Processing record " + (i + 1) + ": " + record);

                    U6 u6 = new U6();

                    // Date handling with detailed error logging
                    String dateStr = String.valueOf(record.get("date"));
                    System.out.println("Date string: " + dateStr);

                    try {
                        u6.setDate(LocalDate.parse(dateStr, formatter));
                    } catch (Exception e1) {
                        try {
                            u6.setDate(LocalDate.parse(dateStr, isoFormatter));
                        } catch (Exception e2) {
                            System.out.println("Date parsing failed for both formats");
                            System.out.println("M/d/yyyy error: " + e1.getMessage());
                            System.out.println("ISO error: " + e2.getMessage());
                            throw new IllegalArgumentException("Invalid date format: " + dateStr);
                        }
                    }

                    // Required fields with validation
                    if (!record.containsKey("projet")) {
                        throw new IllegalArgumentException("Missing required field: projet");
                    }
                    u6.setProjet(String.valueOf(record.get("projet")));

                    if (!record.containsKey("code")) {
                        throw new IllegalArgumentException("Missing required field: code");
                    }
                    u6.setCode(String.valueOf(record.get("code")));

                    // Handle temps d'arrêt variations
                    String tempsDarret = null;
                    if (record.containsKey("temps d'arrêt")) {
                        tempsDarret = String.valueOf(record.get("temps d'arrêt"));
                    } else if (record.containsKey("tempsDarret")) {
                        tempsDarret = String.valueOf(record.get("tempsDarret"));
                    }
                    u6.setTempsDarret(tempsDarret != null ? tempsDarret : "");

                    // Handle total minute variations with type checking
                    Integer totalMinute = null;
                    if (record.containsKey("total minute")) {
                        Object val = record.get("total minute");
                        if (val instanceof Number) {
                            totalMinute = ((Number) val).intValue();
                        } else {
                            totalMinute = Integer.parseInt(String.valueOf(val));
                        }
                    } else if (record.containsKey("totalMinute")) {
                        Object val = record.get("totalMinute");
                        if (val instanceof Number) {
                            totalMinute = ((Number) val).intValue();
                        } else {
                            totalMinute = Integer.parseInt(String.valueOf(val));
                        }
                    }
                    u6.setTotalMinute(totalMinute != null ? totalMinute : 0);

                    // Zone handling
                    String zone = null;
                    if (record.containsKey("zone1")) {
                        zone = String.valueOf(record.get("zone1"));
                    } else if (record.containsKey("zone")) {
                        zone = String.valueOf(record.get("zone"));
                    }
                    u6.setZone(zone != null ? zone : "");

                    // Default commentaire
                    u6.setCommentaire("Accepted");

                    System.out.println("Successfully processed record: " + u6);
                    u6Records.add(u6);

                } catch (Exception e) {
                    System.out.println("Error processing record " + (i + 1) + ": " + e.getMessage());
                    throw new IllegalArgumentException("Error in record " + (i + 1) + ": " + e.getMessage());
                }
            }

            // Save all records
            System.out.println("Attempting to save " + u6Records.size() + " records");
            List<U6> savedRecords = u6Service.saveAll(u6Records);
            System.out.println("Successfully saved all records");

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);

        } catch (Exception e) {
            System.out.println("Final error: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Error processing bulk upload: " + e.getMessage());
            error.put("details", e.getClass().getName());
            error.put("trace", e.getStackTrace().length > 0 ? e.getStackTrace()[0].toString() : "No stack trace");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }
}

