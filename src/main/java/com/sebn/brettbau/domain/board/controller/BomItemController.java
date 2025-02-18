package com.sebn.brettbau.domain.board.controller;

import com.sebn.brettbau.domain.board.dto.BomItemDTO;
import com.sebn.brettbau.domain.board.service.BomItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/bom-items")
public class BomItemController {

    private final BomItemService bomItemService;

    public BomItemController(BomItemService bomItemService) {
        this.bomItemService = bomItemService;
    }

    // Single create
    @PostMapping
    public ResponseEntity<BomItemDTO> createBomItem(@RequestBody BomItemDTO bomItemDTO) {
        BomItemDTO createdBomItem = bomItemService.createBomItem(bomItemDTO);
        return new ResponseEntity<>(createdBomItem, HttpStatus.CREATED);
    }

    // Single update
    @PutMapping("/{id}")
    public ResponseEntity<BomItemDTO> updateBomItem(@PathVariable Long id, @RequestBody BomItemDTO bomItemDTO) {
        BomItemDTO updatedBomItem = bomItemService.updateBomItem(id, bomItemDTO);
        return ResponseEntity.ok(updatedBomItem);
    }

    // Single delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBomItem(@PathVariable Long id) {
        bomItemService.deleteBomItem(id);
        return ResponseEntity.noContent().build();
    }

    // Single get
    @GetMapping("/{id}")
    public ResponseEntity<BomItemDTO> getBomItem(@PathVariable Long id) {
        BomItemDTO bomItemDTO = bomItemService.getBomItemById(id);
        return ResponseEntity.ok(bomItemDTO);
    }

    // ----------------------------------------
    // BULK endpoint: expects a top-level JSON array
    // ----------------------------------------
    @PostMapping("/bulk")
    public ResponseEntity<List<BomItemDTO>> bulkCreateBomItems(@RequestBody List<BomItemDTO> bomItemDTOs) {
        // This method expects something like:
        // [
        //   { "segment": "SEG1", "kurzname": "KM1", ... },
        //   { "segment": "SEG2", "kurzname": "KM2", ... }
        // ]
        List<BomItemDTO> createdItems = bomItemService.bulkCreate(bomItemDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
    }
}
