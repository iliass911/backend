// src/main/java/com/example/backend/domain/changemanagement/controller/ChangeProtocolController.java
package com.sebn.brettbau.domain.changemanagement.controller;

import com.sebn.brettbau.domain.changemanagement.dto.ChangeProtocolDTO;
import com.sebn.brettbau.domain.changemanagement.service.ChangeProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/changeprotocols")
@RequiredArgsConstructor
@CrossOrigin
public class ChangeProtocolController {
    private final ChangeProtocolService service;

    @PostMapping
    public ResponseEntity<ChangeProtocolDTO> create(@RequestBody ChangeProtocolDTO dto) {
        ChangeProtocolDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChangeProtocolDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ChangeProtocolDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChangeProtocolDTO> update(@PathVariable Long id, @RequestBody ChangeProtocolDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

