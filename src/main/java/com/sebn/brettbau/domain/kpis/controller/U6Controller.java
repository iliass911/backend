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
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/u6")
@CrossOrigin(origins = "http://10.150.2.201:3000")
@Validated
public class U6Controller {

    private final U6Service u6Service;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public U6Controller(U6Service u6Service, RoleService roleService, UserService userService) {
        this.u6Service = u6Service;
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<U6> create(@RequestBody @Validated U6 u6) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create KPIs");
        }
        U6 savedRecord = u6Service.save(u6);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }

    @GetMapping
    public ResponseEntity<List<U6>> getAll() {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read KPIs");
        }
        List<U6> records = u6Service.findAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<U6> getById(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.READ)) {
            throw new AccessDeniedException("No permission to read KPIs");
        }
        Optional<U6> record = u6Service.findById(id);
        return record.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

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

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody List<Map<String, Object>> records) {
        User currentUser = userService.getCurrentUser();
        if (!roleService.roleHasPermission(currentUser.getRole(), Module.KPIS, PermissionType.CREATE)) {
            throw new AccessDeniedException("No permission to create KPIs");
        }
        try {
            System.out.println("Received " + records.size() + " records");
            List<U6> u6Records = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE;
            for (int i = 0; i < records.size(); i++) {
                try {
                    Map<String, Object> record = records.get(i);
                    System.out.println("Processing record " + (i + 1) + ": " + record);
                    U6 u6 = new U6();
                    String dateStr = String.valueOf(record.get("date"));
                    try {
                        u6.setDate(LocalDate.parse(dateStr, formatter));
                    } catch (Exception e1) {
                        try {
                            u6.setDate(LocalDate.parse(dateStr, isoFormatter));
                        } catch (Exception e2) {
                            throw new IllegalArgumentException("Invalid date format: " + dateStr);
                        }
                    }
                    u6.setMois(record.get("mois") != null ? String.valueOf(record.get("mois")) : "");
                    u6.setSemaine(record.get("semaine") != null ? String.valueOf(record.get("semaine")) : "");
                    u6.setEquipment(record.get("equipment") != null ? String.valueOf(record.get("equipment")) : "");
                    u6.setEquipmentName(record.get("equipmentName") != null ? String.valueOf(record.get("equipmentName")) : "");
                    u6.setSite(record.get("site") != null ? String.valueOf(record.get("site")) : "");
                    u6.setProjet(record.get("projet") != null ? String.valueOf(record.get("projet")) : "");
                    u6.setShift(record.get("shift") != null ? String.valueOf(record.get("shift")) : "");
                    
                    Object duree = record.get("dureeArret");
                    if (duree != null) {
                        if (duree instanceof Number) {
                            u6.setDureeArret(((Number) duree).intValue());
                        } else {
                            u6.setDureeArret(Integer.parseInt(String.valueOf(duree)));
                        }
                    } else {
                        u6.setDureeArret(0);
                    }
                    
                    u6.setAccepte(record.get("accepte") != null ? String.valueOf(record.get("accepte")) : "");
                    
                    Object nbr = record.get("nbrOperateurs");
                    if (nbr != null) {
                        if (nbr instanceof Number) {
                            u6.setNbrOperateurs(((Number) nbr).intValue());
                        } else {
                            u6.setNbrOperateurs(Integer.parseInt(String.valueOf(nbr)));
                        }
                    } else {
                        u6.setNbrOperateurs(1);
                    }
                    
                    u6.setDescriptionDefaillance(record.get("descriptionDefaillance") != null ? String.valueOf(record.get("descriptionDefaillance")) : "");
                    u6.setDescriptionActionCorrective(record.get("descriptionActionCorrective") != null ? String.valueOf(record.get("descriptionActionCorrective")) : "");
                    u6.setIntervenant(record.get("intervenant") != null ? String.valueOf(record.get("intervenant")) : "");
                    u6.setTypeEquipement(record.get("typeEquipement") != null ? String.valueOf(record.get("typeEquipement")) : "");
                    u6.setNatureIntervention(record.get("natureIntervention") != null ? String.valueOf(record.get("natureIntervention")) : "");
                    u6.setLienScanFicheBDE(record.get("lienScanFicheBDE") != null ? String.valueOf(record.get("lienScanFicheBDE")) : "");
                    
                    // Optionally, set equipment-specific fields if provided
                    if (record.get("installationDate") != null) {
                        String instDateStr = String.valueOf(record.get("installationDate"));
                        try {
                            u6.setInstallationDate(LocalDate.parse(instDateStr, formatter));
                        } catch(Exception e) {
                            try {
                                u6.setInstallationDate(LocalDate.parse(instDateStr, isoFormatter));
                            } catch(Exception ex) {
                                System.out.println("Installation date parsing failed: " + ex.getMessage());
                                u6.setInstallationDate(null);
                            }
                        }
                    }
                    u6.setEquipmentStatus(record.get("equipmentStatus") != null ? String.valueOf(record.get("equipmentStatus")) : "");
                    
                    // Set recordType if provided; otherwise, default to "downtime"
                    u6.setRecordType(record.get("recordType") != null ? String.valueOf(record.get("recordType")) : "downtime");

                    System.out.println("Successfully processed record: " + u6);
                    u6Records.add(u6);
                } catch (Exception e) {
                    System.out.println("Error processing record " + (i + 1) + ": " + e.getMessage());
                    throw new IllegalArgumentException("Error in record " + (i + 1) + ": " + e.getMessage());
                }
            }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Dashboard analysis endpoint.
     * Computes metrics such as total failures, total downtime, average downtime,
     * MTTR, MTBF, monthly failure counts, and weekly downtime trends.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        List<U6> records = u6Service.findAll();
        Map<String, Object> metrics = new HashMap<>();
        
        // Only consider downtime records (recordType = "downtime")
        List<U6> downtimeRecords = new ArrayList<>();
        for (U6 r : records) {
            if ("downtime".equalsIgnoreCase(r.getRecordType())) {
                downtimeRecords.add(r);
            }
        }
        
        int totalFailures = downtimeRecords.size();
        metrics.put("totalFailures", totalFailures);
        
        int totalDowntime = downtimeRecords.stream()
                              .mapToInt(r -> r.getDureeArret() != null ? r.getDureeArret() : 0)
                              .sum();
        metrics.put("totalDowntime", totalDowntime);
        
        double avgDowntime = totalFailures > 0 ? (double) totalDowntime / totalFailures : 0;
        metrics.put("avgDowntime", avgDowntime);
        
        // MTTR: average downtime in hours
        double mttr = avgDowntime / 60.0;
        metrics.put("mttr", mttr);
        
        // MTBF: (Total period minutes minus total downtime) divided by number of failures
        Optional<LocalDate> minDateOpt = downtimeRecords.stream().map(U6::getDate).filter(Objects::nonNull).min(LocalDate::compareTo);
        Optional<LocalDate> maxDateOpt = downtimeRecords.stream().map(U6::getDate).filter(Objects::nonNull).max(LocalDate::compareTo);
        if (minDateOpt.isPresent() && maxDateOpt.isPresent()) {
            LocalDate minDate = minDateOpt.get();
            LocalDate maxDate = maxDateOpt.get();
            long periodMinutes = Duration.between(minDate.atStartOfDay(), maxDate.plusDays(1).atStartOfDay()).toMinutes();
            long operatingTime = periodMinutes - totalDowntime;
            double mtbf = totalFailures > 0 ? (double) operatingTime / totalFailures : 0;
            metrics.put("mtbf", mtbf / 60.0); // in hours
        } else {
            metrics.put("mtbf", 0);
        }
        
        // Monthly failure counts (using "mois" or extract from date)
        Map<String, Integer> monthlyFailures = new HashMap<>();
        for (U6 r : downtimeRecords) {
            String month = r.getMois();
            if (month == null || month.isEmpty()) {
                if (r.getDate() != null) {
                    month = r.getDate().toString().substring(0, 7);
                } else {
                    month = "Unknown";
                }
            }
            monthlyFailures.put(month, monthlyFailures.getOrDefault(month, 0) + 1);
        }
        List<Map<String, Object>> monthlyData = new ArrayList<>();
        monthlyFailures.forEach((month, count) -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", month);
            entry.put("failures", count);
            monthlyData.add(entry);
        });
        metrics.put("monthlyFailures", monthlyData);
        
        // Weekly downtime trends: group by week number from date
        Map<String, Integer> weeklyDowntime = new HashMap<>();
        for (U6 r : downtimeRecords) {
            if (r.getDate() != null) {
                Date d = java.sql.Date.valueOf(r.getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                int year = cal.get(Calendar.YEAR);
                String key = year + "-W" + week;
                weeklyDowntime.put(key, weeklyDowntime.getOrDefault(key, 0) + (r.getDureeArret() != null ? r.getDureeArret() : 0));
            }
        }
        List<Map<String, Object>> weeklyData = new ArrayList<>();
        weeklyDowntime.forEach((week, downtime) -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("week", week);
            entry.put("downtime", downtime);
            weeklyData.add(entry);
        });
        metrics.put("weeklyDowntime", weeklyData);
        
        return ResponseEntity.ok(metrics);
    }
}
