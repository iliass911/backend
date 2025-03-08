package com.sebn.brettbau.domain.board.controller;

import com.sebn.brettbau.domain.board.dto.BomItemDTO;
import com.sebn.brettbau.domain.board.service.BomItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;

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

    // BULK endpoint: expects a top-level JSON array
    @PostMapping("/bulk")
    public ResponseEntity<List<BomItemDTO>> bulkCreateBomItems(@RequestBody List<BomItemDTO> bomItemDTOs) {
        List<BomItemDTO> createdItems = bomItemService.bulkCreate(bomItemDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
    }

    // BULK UPLOAD endpoint: accepts a CSV file and creates BOM items
    @PostMapping(value = "/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<BomItemDTO>> bulkUploadBomItems(@RequestParam("file") MultipartFile file) {
        try {
            List<BomItemDTO> bomItemDTOs = parseCsvFile(file);
            List<BomItemDTO> createdItems = bomItemService.bulkCreate(bomItemDTOs);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItems);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Simple CSV parser: expects CSV with header and exactly 10 columns.
    private List<BomItemDTO> parseCsvFile(MultipartFile file) throws Exception {
        List<BomItemDTO> bomItemDTOs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }
                String[] values = line.split(",");
                if (values.length < 10) continue; // ensure there are enough columns
                BomItemDTO dto = new BomItemDTO();
                dto.setSegment(values[0]);
                dto.setKurzname(values[1]);
                dto.setIdentMatchcode(values[2]);
                dto.setModelType(values[3]);
                dto.setSesamNumber(values[4]);
                dto.setMissingOnBoard(Boolean.parseBoolean(values[5]));
                dto.setQuantityOnBoard(Integer.parseInt(values[6]));
                dto.setObservation(values[7]);
                dto.setPrice(Double.parseDouble(values[8]));
                dto.setMotif(values[9]);
                bomItemDTOs.add(dto);
            }
        }
        return bomItemDTOs;
    }
}
