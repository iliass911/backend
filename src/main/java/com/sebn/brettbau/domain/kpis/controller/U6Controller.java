package com.sebn.brettbau.domain.kpis.controller;

import com.sebn.brettbau.domain.kpis.entity.U6;
import com.sebn.brettbau.domain.kpis.service.U6Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/u6")
public class U6Controller {

    @Autowired
    private U6Service u6Service;

    @GetMapping
    public ResponseEntity<List<U6>> getAll() {
        List<U6> records = u6Service.findAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<U6> getById(@PathVariable Long id) {
        Optional<U6> record = u6Service.findById(id);
        return record.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<U6> create(@RequestBody U6 u6) {
        U6 savedRecord = u6Service.save(u6);
        return ResponseEntity.ok(savedRecord);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<U6>> createBulk(@RequestBody List<U6> u6List) {
        List<U6> savedRecords = u6Service.saveAll(u6List);
        return ResponseEntity.ok(savedRecords);
    }

    @PutMapping("/{id}")
    public ResponseEntity<U6> update(@PathVariable Long id, @RequestBody U6 u6) {
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
        Optional<U6> existingRecord = u6Service.findById(id);
        if (existingRecord.isPresent()) {
            u6Service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}