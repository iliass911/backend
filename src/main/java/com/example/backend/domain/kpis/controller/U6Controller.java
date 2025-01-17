// U6Controller.java
package com.example.backend.domain.kpis.controller;

import com.example.backend.common.BaseController;
import com.example.backend.domain.kpis.entity.U6;
import com.example.backend.domain.kpis.service.U6Service;
import com.example.backend.domain.role.service.PermissionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/u6")
public class U6Controller extends BaseController {

    @Autowired
    private U6Service u6Service;
    
    public U6Controller(PermissionChecker permissionChecker) {
        super(permissionChecker);
    }

    @GetMapping
    public ResponseEntity<List<U6>> getAll() {
        checkPermission("U6", "VIEW");
        List<U6> records = u6Service.findAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<U6> getById(@PathVariable Long id) {
        checkPermission("U6", "VIEW");
        Optional<U6> record = u6Service.findById(id);
        return record.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<U6> create(@RequestBody U6 u6) {
        checkPermission("U6", "CREATE");
        U6 savedRecord = u6Service.save(u6);
        return ResponseEntity.ok(savedRecord);
    }  

    @PostMapping("/bulk")
    public ResponseEntity<List<U6>> createBulk(@RequestBody List<U6> u6List) {
        checkPermission("U6", "CREATE");
        List<U6> savedRecords = u6Service.saveAll(u6List);
        return ResponseEntity.ok(savedRecords);
    }

    @PutMapping("/{id}")
    public ResponseEntity<U6> update(@PathVariable Long id, @RequestBody U6 u6) {
        checkPermission("U6", "UPDATE");
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
        checkPermission("U6", "DELETE");  
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6Service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}