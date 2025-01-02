package com.example.backend.domain.bom.service;

import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.entity.Bom;
import com.example.backend.domain.bom.entity.BomLine;
import com.example.backend.domain.bom.entity.BomLineUnit;
import com.example.backend.domain.bom.mapper.BomMapper;
import com.example.backend.domain.bom.repository.BomRepository;
import com.example.backend.domain.bom.repository.BomLineRepository;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import com.example.backend.domain.preventive_maintenance.repository.BoardRepository;
import com.example.backend.domain.inventory.entity.InventoryItem;
import com.example.backend.domain.inventory.repository.InventoryItemRepository;
import com.example.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BomService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BomRepository bomRepository;
    private final BomLineRepository bomLineRepository;
    private final BoardRepository boardRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final BomMapper bomMapper;

    @Transactional
    public BomDTO createBom(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));

        if (bomRepository.findByBoardId(boardId).isPresent()) {
            throw new IllegalArgumentException("BOM already exists for this Board.");
        }

        Bom bom = Bom.builder()
                .board(board)
                .totalCost(0.0)
                .bomLines(new HashSet<>())
                .build();

        Bom saved = bomRepository.save(bom);
        entityManager.flush();
        entityManager.refresh(saved);
        
        return bomMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public BomDTO getBomByBoardId(Long boardId) {
        Bom bom = bomRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found for board id: " + boardId));
        return bomMapper.toDTO(bom);
    }

    @Transactional(readOnly = true)
    public BomDTO getBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));
        return bomMapper.toDTO(bom);
    }

    @Transactional
    public BomDTO addOrUpdateBomLines(Long bomId, Set<BomLineDTO> bomLineDTOs) {
        System.out.println("Starting BOM update process...");
        
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

        // Clear existing lines
        if (bom.getBomLines() != null) {
            for (BomLine line : new HashSet<>(bom.getBomLines())) {
                entityManager.remove(line);
            }
            bom.getBomLines().clear();
            entityManager.flush();
        }

        // Reset total cost
        double totalCost = 0.0;

        // Process new lines
        for (BomLineDTO lineDto : bomLineDTOs) {
            BomLine line = new BomLine();
            line.setBom(bom);

            // Set basic properties
            if (lineDto.getInventoryItemId() != null) {
                InventoryItem item = inventoryItemRepository.findById(lineDto.getInventoryItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found"));
                line.setInventoryItem(item);
                line.setUnitPrice(item.getPrice());
                line.setComponentName(item.getRefCode());
            } else {
                line.setUnitPrice(lineDto.getUnitPrice());
                line.setComponentName(lineDto.getComponentName());
            }

            line.setCategory(lineDto.getCategory());
            line.setQuantity(lineDto.getQuantity() != null ? lineDto.getQuantity() : 1);

            // Calculate line cost
            double lineCost = line.getUnitPrice() * line.getQuantity();
            line.setLineCost(lineCost);
            totalCost += lineCost;

            // Process unit names
            List<String> unitNames = lineDto.getUnitNames();
            if (unitNames == null || unitNames.isEmpty()) {
                unitNames = new ArrayList<>();
                for (int i = 0; i < line.getQuantity(); i++) {
                    unitNames.add("Unit " + (i + 1));
                }
            }

            // Create units
            List<BomLineUnit> units = new ArrayList<>();
            for (int i = 0; i < unitNames.size(); i++) {
                BomLineUnit unit = BomLineUnit.builder()
                        .bomLine(line)
                        .unitName(unitNames.get(i))
                        .unitIndex(i + 1)
                        .build();
                units.add(unit);
            }
            line.setUnits(units);

            // Persist the line
            entityManager.persist(line);
            bom.getBomLines().add(line);
        }

        bom.setTotalCost(totalCost);
        
        // Save and refresh
        Bom savedBom = bomRepository.save(bom);
        entityManager.flush();
        entityManager.refresh(savedBom);

        // Load all units eagerly for verification
        for (BomLine line : savedBom.getBomLines()) {
            line.getUnits().size(); // Force load
        }

        return bomMapper.toDTO(savedBom);
    }

    @Transactional
    public void deleteBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

        bomRepository.delete(bom);
        entityManager.flush();
    }
}