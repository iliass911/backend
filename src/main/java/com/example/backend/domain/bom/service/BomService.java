package com.example.backend.domain.bom.service;

import com.example.backend.domain.bom.dto.BomDTO;
import com.example.backend.domain.bom.dto.BomLineDTO;
import com.example.backend.domain.bom.entity.Bom;
import com.example.backend.domain.bom.entity.BomLine;
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

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BomService {

    private final BomRepository bomRepository;
    private final BomLineRepository bomLineRepository;
    private final BoardRepository boardRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final BomMapper bomMapper;

    /**
     * Create or link a BOM to a board.
     */
    public BomDTO createBom(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + boardId));

        // Check if BOM already exists for the Board using the repository method
        if (bomRepository.findByBoardId(boardId).isPresent()) {
            throw new IllegalArgumentException("BOM already exists for this Board.");
        }

        // Create a new BOM
        Bom bom = Bom.builder()
                .board(board)
                .totalCost(0.0)
                .bomLines(new HashSet<>())
                .build();
        Bom saved = bomRepository.save(bom);
        return bomMapper.toDTO(saved);
    }

    /**
     * Get BOM by Board ID
     */
    public BomDTO getBomByBoardId(Long boardId) {
        Bom bom = bomRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found for board id: " + boardId));
        return bomMapper.toDTO(bom);
    }

    /**
     * Retrieve a BOM by ID.
     */
    public BomDTO getBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));
        return bomMapper.toDTO(bom);
    }

    /**
     * Add or update BOM lines for a given BOM, including handling unit names and String IDs.
     */
    @Transactional
    public BomDTO addOrUpdateBomLines(Long bomId, Set<BomLineDTO> bomLineDTOs) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

        // Clear existing lines
        if (bom.getBomLines() != null && !bom.getBomLines().isEmpty()) {
            bom.getBomLines().forEach(line -> {
                line.setBom(null);
                if (line.getUnits() != null) {
                    line.getUnits().forEach(unit -> unit.setBomLine(null));
                }
            });
            bom.getBomLines().clear();
            bomRepository.save(bom);  // Save the cleared state
        }

        // Initialize new collection if null
        if (bom.getBomLines() == null) {
            bom.setBomLines(new HashSet<>());
        }

        double totalCost = 0.0;
        Set<BomLine> newLines = new HashSet<>();

        for (BomLineDTO lineDto : bomLineDTOs) {
            BomLine line;

            // If it's not a temporary ID, try to find existing line
            if (!lineDto.isTemporary() && lineDto.getId() != null) {
                try {
                    Long lineId = Long.parseLong(lineDto.getId());
                    line = bomLineRepository.findById(lineId)
                            .orElse(new BomLine());
                } catch (NumberFormatException e) {
                    // If ID is not a valid Long, treat it as a new line
                    line = new BomLine();
                }
            } else {
                line = new BomLine();
            }

            line.setBom(bom);

            // Handle inventory item
            if (lineDto.getInventoryItemId() != null) {
                InventoryItem item = inventoryItemRepository.findById(lineDto.getInventoryItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found with id: " + lineDto.getInventoryItemId()));
                line.setInventoryItem(item);
                line.setUnitPrice(item.getPrice());
                line.setComponentName(item.getRefCode());
            } else {
                // Manually set price or from BOMLineDTO
                line.setUnitPrice(lineDto.getUnitPrice());
                line.setComponentName(lineDto.getComponentName());
            }

            // Set category
            line.setCategory(lineDto.getCategory());

            // Set quantity (with null check)
            line.setQuantity(lineDto.getQuantity() != null ? lineDto.getQuantity() : 1);

            // Calculate line cost (with null checks)
            if (line.getUnitPrice() != null && line.getQuantity() != null) {
                line.setLineCost(line.getUnitPrice() * line.getQuantity());
                totalCost += line.getLineCost();
            } else {
                line.setLineCost(0.0);
            }

            // Handle unit names
            if (lineDto.getUnitNames() != null) {
                line.setUnitNames(lineDto.getUnitNames());
            }

            newLines.add(line);
            bom.getBomLines().add(line);  // Add to both collections
        }

        // Update the BOM's total cost
        bom.setTotalCost(totalCost);

        // Save BOM which will cascade to lines due to CascadeType.ALL
        Bom savedBom = bomRepository.save(bom);

        // Clear persistence context to avoid stale data
        bomRepository.flush();

        return bomMapper.toDTO(savedBom);
    }

    /**
     * Delete a BOM entirely.
     */
    @Transactional
    public void deleteBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

        // Clear associations first
        if (bom.getBomLines() != null && !bom.getBomLines().isEmpty()) {
            bom.getBomLines().forEach(line -> {
                line.setBom(null);
                if (line.getUnits() != null) {
                    line.getUnits().forEach(unit -> unit.setBomLine(null));
                }
            });
            bom.getBomLines().clear();
        }

        bomRepository.delete(bom);
    }
}
