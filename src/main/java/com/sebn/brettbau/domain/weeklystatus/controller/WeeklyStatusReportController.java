package com.sebn.brettbau.domain.weeklystatus.controller;

import com.sebn.brettbau.domain.weeklystatus.dto.WeeklyStatusReportDTO;
import com.sebn.brettbau.domain.weeklystatus.service.WeeklyStatusReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-status")
@RequiredArgsConstructor
public class WeeklyStatusReportController {
    private final WeeklyStatusReportService service;

    @GetMapping
    public ResponseEntity<List<WeeklyStatusReportDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<WeeklyStatusReportDTO> create(@RequestBody WeeklyStatusReportDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeeklyStatusReportDTO> update(@PathVariable Long id, @RequestBody WeeklyStatusReportDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
