package com.sebn.brettbau.domain.bom.service;

import com.sebn.brettbau.domain.bom.dto.BomDTO;
import com.sebn.brettbau.domain.bom.dto.BomLineDTO;
import com.sebn.brettbau.domain.bom.entity.Bom;
import com.sebn.brettbau.domain.bom.entity.BomLine;
import com.sebn.brettbau.domain.bom.mapper.BomMapper;
import com.sebn.brettbau.domain.bom.repository.BomLineRepository;
import com.sebn.brettbau.domain.bom.repository.BomRepository;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import com.sebn.brettbau.domain.inventory.repository.InventoryItemRepository;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardFamilyRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BomService {
    private final BomRepository bomRepository;
    private final BomLineRepository bomLineRepository;
    private final BoardRepository boardRepository;
    private final BoardFamilyRepository boardFamilyRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final BomMapper bomMapper;

    // Existing Methods ...

    /**
     * Creates a new BOM for a specific board.
     *
     * @param boardId The ID of the board.
     * @return The created BomDTO.
     */
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
        return bomMapper.toDTO(bomRepository.save(bom));
    }

    /**
     * Retrieves a BOM by board ID.
     *
     * @param boardId The ID of the board.
     * @return The BomDTO.
     */
    public BomDTO getBomByBoardId(Long boardId) {
        Bom bom = bomRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found for board id: " + boardId));
        return bomMapper.toDTO(bom);
    }

    /**
     * Retrieves a BOM by BOM ID.
     *
     * @param bomId The ID of the BOM.
     * @return The BomDTO.
     */
    public BomDTO getBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));
        return bomMapper.toDTO(bom);
    }

    /**
     * Adds or updates BOM lines for a specific BOM.
     *
     * @param bomId        The ID of the BOM.
     * @param bomLineDTOs  The set of BOM line DTOs.
     * @return The updated BomDTO.
     */
    @Transactional
    public BomDTO addOrUpdateBomLines(Long bomId, Set<BomLineDTO> bomLineDTOs) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

        // Clear existing BOM lines
        if (bom.getBomLines() != null && !bom.getBomLines().isEmpty()) {
            bom.getBomLines().forEach(line -> {
                line.setBom(null);
                if (line.getUnits() != null) {
                    line.getUnits().forEach(unit -> unit.setBomLine(null));
                }
            });
            bom.getBomLines().clear();
            bomRepository.save(bom);
        }

        if (bom.getBomLines() == null) {
            bom.setBomLines(new HashSet<>());
        }

        double totalCost = 0.0;

        for (BomLineDTO lineDto : bomLineDTOs) {
            BomLine line = new BomLine();
            if (!lineDto.isTemporary() && lineDto.getId() != null) {
                try {
                    Long lineId = Long.parseLong(lineDto.getId());
                    line = bomLineRepository.findById(lineId).orElse(new BomLine());
                } catch (NumberFormatException e) {
                    line = new BomLine();
                }
            }

            line.setBom(bom);

            if (lineDto.getInventoryItemId() != null) {
                InventoryItem item = inventoryItemRepository.findById(lineDto.getInventoryItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found with id: " + lineDto.getInventoryItemId()));
                line.setInventoryItem(item);
                line.setUnitPrice(item.getPrice());
                line.setComponentName(item.getRefCode());
            } else {
                line.setUnitPrice(lineDto.getUnitPrice());
                line.setComponentName(lineDto.getComponentName());
            }

            line.setCategory(lineDto.getCategory());
            line.setQuantity(lineDto.getQuantity() != null ? lineDto.getQuantity() : 1);

            if (line.getUnitPrice() != null && line.getQuantity() != null) {
                line.setLineCost(line.getUnitPrice() * line.getQuantity());
                totalCost += line.getLineCost();
            } else {
                line.setLineCost(0.0);
            }

            if (lineDto.getUnitNames() != null) {
                line.setUnitNames(lineDto.getUnitNames());
            }

            bom.getBomLines().add(line);
        }

        bom.setTotalCost(totalCost);
        return bomMapper.toDTO(bomRepository.save(bom));
    }

    /**
     * Deletes a BOM by its ID.
     *
     * @param bomId The ID of the BOM to delete.
     */
    @Transactional
    public void deleteBom(Long bomId) {
        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM not found with id: " + bomId));

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

    // Family Methods

    /**
     * Creates BOMs for all boards in a specific family.
     *
     * @param familyId The ID of the BoardFamily.
     * @return A list of created BomDTOs.
     */
    @Transactional
    public List<BomDTO> createBomForFamily(Long familyId) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));

        List<Board> familyBoards = boardRepository.findByFamilyId(familyId);
        if (familyBoards.isEmpty()) {
            throw new IllegalStateException("No boards found in family with id: " + familyId);
        }

        List<BomDTO> createdBoms = new ArrayList<>();
        for (Board board : familyBoards) {
            if (bomRepository.findByBoardId(board.getId()).isPresent()) {
                continue;
            }

            Bom bom = Bom.builder()
                    .board(board)
                    .totalCost(0.0)
                    .bomLines(new HashSet<>())
                    .build();
            createdBoms.add(bomMapper.toDTO(bomRepository.save(bom)));
        }

        return createdBoms;
    }

    /**
     * Retrieves a template BOM for a specific family.
     *
     * @param familyId The ID of the BoardFamily.
     * @return The BomDTO.
     */
    public BomDTO getBomByFamilyId(Long familyId) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));

        List<Board> familyBoards = boardRepository.findByFamilyId(familyId);
        if (familyBoards.isEmpty()) {
            throw new IllegalStateException("No boards found in family with id: " + familyId);
        }

        Board firstBoard = familyBoards.get(0);
        Bom templateBom = bomRepository.findByBoardId(firstBoard.getId())
                .orElseGet(() -> {
                    Bom newBom = Bom.builder()
                            .board(firstBoard)
                            .totalCost(0.0)
                            .bomLines(new HashSet<>())
                            .build();
                    return bomRepository.save(newBom);
                });

        return bomMapper.toDTO(templateBom);
    }

    /**
     * Adds or updates BOM lines for all BOMs within a family.
     *
     * @param familyId     The ID of the BoardFamily.
     * @param bomLineDTOs  The set of BOM line DTOs.
     * @return A list of updated BomDTOs.
     */
    @Transactional
    public List<BomDTO> addOrUpdateFamilyBomLines(Long familyId, Set<BomLineDTO> bomLineDTOs) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));

        List<Board> familyBoards = boardRepository.findByFamilyId(familyId);
        if (familyBoards.isEmpty()) {
            throw new IllegalStateException("No boards found in family with id: " + familyId);
        }

        List<BomDTO> updatedBoms = new ArrayList<>();
        for (Board board : familyBoards) {
            Bom bom = bomRepository.findByBoardId(board.getId())
                    .orElseGet(() -> {
                        Bom newBom = Bom.builder()
                                .board(board)
                                .totalCost(0.0)
                                .bomLines(new HashSet<>())
                                .build();
                        return bomRepository.save(newBom);
                    });

            updatedBoms.add(addOrUpdateBomLines(bom.getId(), bomLineDTOs));
        }

        return updatedBoms;
    }

    /**
     * Deletes all BOMs associated with a specific family.
     *
     * @param familyId The ID of the BoardFamily.
     */
    @Transactional
    public void deleteFamilyBoms(Long familyId) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));

        List<Board> familyBoards = boardRepository.findByFamilyId(familyId);
        for (Board board : familyBoards) {
            bomRepository.findByBoardId(board.getId())
                    .ifPresent(bom -> deleteBom(bom.getId()));
        }
    }

    // New Method: saveBOMLines

    /**
     * Saves a list of BOM lines for a specific BoardFamily.
     *
     * @param family The BoardFamily entity.
     * @param lines  The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @Transactional
    public List<BomLineDTO> saveBOMLines(BoardFamily family, List<BomLineDTO> lines) {
        BoardFamily existingFamily = boardFamilyRepository.findById(family.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BoardFamily not found with id: " + family.getId()));

        final Bom templateBom = bomRepository.save(Bom.builder()
                .totalCost(0.0)
                .bomLines(new HashSet<>())
                .build());

        List<BomLine> bomLines = lines.stream()
                .map(dto -> BomLine.builder()
                    .category(dto.getCategory())
                    .componentName(dto.getComponentName())
                    .quantity(dto.getQuantity())
                    .unitPrice(dto.getUnitPrice())
                    .bom(templateBom)
                    .build())
                .collect(Collectors.toList());

        List<BomLine> savedLines = bomLineRepository.saveAll(bomLines);

        return savedLines.stream()
                .map(line -> BomLineDTO.builder()
                        .id(line.getId().toString())
                        .category(line.getCategory())
                        .componentName(line.getComponentName())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
    }
    /**
     * Overloaded method to save BOM lines using family ID.
     *
     * @param familyId The ID of the BoardFamily.
     * @param lines    The list of BomLineDTOs to save.
     * @return The list of saved BomLineDTOs.
     */
    @Transactional
    public List<BomLineDTO> saveBOMLines(Long familyId, List<BomLineDTO> lines) {
        BoardFamily family = boardFamilyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("BoardFamily not found with id: " + familyId));

        return saveBOMLines(family, lines);
    }
}

