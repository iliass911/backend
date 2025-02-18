package com.sebn.brettbau.domain.board.service;

import com.sebn.brettbau.domain.board.dto.BomComparisonItemDTO;
import com.sebn.brettbau.domain.board.dto.BomItemDTO;
import com.sebn.brettbau.domain.board.entity.BoardFamily;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import com.sebn.brettbau.domain.board.repository.BoardFamilyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BomComparisonService {

    private final BoardFamilyRepository boardFamilyRepository;

    public BomComparisonService(BoardFamilyRepository boardFamilyRepository) {
        this.boardFamilyRepository = boardFamilyRepository;
    }

    @Transactional(readOnly = true)
    public List<BomComparisonItemDTO> compareBomItems(Long family1Id, Long family2Id) {
        // Use the fetch join method to load BOM items eagerly
        BoardFamily family1 = boardFamilyRepository.findByIdWithBomItems(family1Id);
        if (family1 == null) {
            throw new ResourceNotFoundException("Board family not found: " + family1Id);
        }
        BoardFamily family2 = boardFamilyRepository.findByIdWithBomItems(family2Id);
        if (family2 == null) {
            throw new ResourceNotFoundException("Board family not found: " + family2Id);
        }

        // Convert BOM items from each family to DTOs
        List<BomItemDTO> bomList1 = family1.getBomItems().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        List<BomItemDTO> bomList2 = family2.getBomItems().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // Create multimap-like structures to handle duplicate matchcodes
        Map<String, List<BomItemDTO>> map1 = bomList1.stream()
                .collect(Collectors.groupingBy(BomItemDTO::getIdentMatchcode));
        Map<String, List<BomItemDTO>> map2 = bomList2.stream()
                .collect(Collectors.groupingBy(BomItemDTO::getIdentMatchcode));

        List<BomComparisonItemDTO> comparisons = new ArrayList<>();

        // Process items added in family2
        for (String matchcode : map2.keySet()) {
            if (!map1.containsKey(matchcode)) {
                // All items with this matchcode in family2 are new
                for (BomItemDTO item2 : map2.get(matchcode)) {
                    BomComparisonItemDTO dto = new BomComparisonItemDTO();
                    dto.setItem1(null);
                    dto.setItem2(item2);
                    dto.setComparisonType("ADDED");
                    comparisons.add(dto);
                }
            }
        }

        // Process items removed from family2
        for (String matchcode : map1.keySet()) {
            if (!map2.containsKey(matchcode)) {
                // All items with this matchcode in family1 are removed
                for (BomItemDTO item1 : map1.get(matchcode)) {
                    BomComparisonItemDTO dto = new BomComparisonItemDTO();
                    dto.setItem1(item1);
                    dto.setItem2(null);
                    dto.setComparisonType("REMOVED");
                    comparisons.add(dto);
                }
            }
        }

        // Process modified items
        for (String matchcode : map1.keySet()) {
            if (map2.containsKey(matchcode)) {
                List<BomItemDTO> items1 = map1.get(matchcode);
                List<BomItemDTO> items2 = map2.get(matchcode);

                // Compare each item from family1 with the best matching item from family2
                for (BomItemDTO item1 : items1) {
                    BomItemDTO bestMatch = findBestMatch(item1, items2);
                    if (bestMatch != null) {
                        List<String> changedFields = findChangedFields(item1, bestMatch);
                        if (!changedFields.isEmpty()) {
                            BomComparisonItemDTO dto = new BomComparisonItemDTO();
                            dto.setItem1(item1);
                            dto.setItem2(bestMatch);
                            dto.setComparisonType("MODIFIED");
                            dto.setModifiedFields(changedFields);
                            comparisons.add(dto);
                        }
                        // Remove the matched item to avoid duplicate comparisons
                        items2.remove(bestMatch);
                    }
                }

                // Any remaining items in items2 are considered as additions
                for (BomItemDTO unmatchedItem2 : items2) {
                    BomComparisonItemDTO dto = new BomComparisonItemDTO();
                    dto.setItem1(null);
                    dto.setItem2(unmatchedItem2);
                    dto.setComparisonType("ADDED");
                    comparisons.add(dto);
                }
            }
        }

        return comparisons;
    }

    private BomItemDTO findBestMatch(BomItemDTO source, List<BomItemDTO> candidates) {
        if (candidates.isEmpty()) {
            return null;
        }

        // Find the item with the most matching fields
        return candidates.stream()
                .max(Comparator.comparingInt(candidate -> countMatchingFields(source, candidate)))
                .orElse(candidates.get(0));
    }

    private int countMatchingFields(BomItemDTO item1, BomItemDTO item2) {
        int matches = 0;
        if (Objects.equals(item1.getSegment(), item2.getSegment())) matches++;
        if (Objects.equals(item1.getKurzname(), item2.getKurzname())) matches++;
        if (Objects.equals(item1.getModelType(), item2.getModelType())) matches++;
        if (Objects.equals(item1.getSesamNumber(), item2.getSesamNumber())) matches++;
        if (Objects.equals(item1.getMissingOnBoard(), item2.getMissingOnBoard())) matches++;
        if (Objects.equals(item1.getQuantityOnBoard(), item2.getQuantityOnBoard())) matches++;
        if (Objects.equals(item1.getObservation(), item2.getObservation())) matches++;
        if (Objects.equals(item1.getPrice(), item2.getPrice())) matches++;
        return matches;
    }

    private List<String> findChangedFields(BomItemDTO item1, BomItemDTO item2) {
        List<String> changedFields = new ArrayList<>();
        if (!Objects.equals(item1.getSegment(), item2.getSegment())) {
            changedFields.add("segment");
        }
        if (!Objects.equals(item1.getKurzname(), item2.getKurzname())) {
            changedFields.add("kurzname");
        }
        if (!Objects.equals(item1.getModelType(), item2.getModelType())) {
            changedFields.add("modelType");
        }
        if (!Objects.equals(item1.getSesamNumber(), item2.getSesamNumber())) {
            changedFields.add("sesamNumber");
        }
        if (!Objects.equals(item1.getMissingOnBoard(), item2.getMissingOnBoard())) {
            changedFields.add("missingOnBoard");
        }
        if (!Objects.equals(item1.getQuantityOnBoard(), item2.getQuantityOnBoard())) {
            changedFields.add("quantityOnBoard");
        }
        if (!Objects.equals(item1.getObservation(), item2.getObservation())) {
            changedFields.add("observation");
        }
        if (!Objects.equals(item1.getPrice(), item2.getPrice())) {
            changedFields.add("price");
        }
        return changedFields;
    }

    private BomItemDTO convertToDto(com.sebn.brettbau.domain.board.entity.BomItem bomItem) {
        BomItemDTO dto = new BomItemDTO();
        dto.setId(bomItem.getId());
        dto.setBoardFamilyId(bomItem.getBoardFamily().getId());
        dto.setSegment(bomItem.getSegment());
        dto.setKurzname(bomItem.getKurzname());
        dto.setIdentMatchcode(bomItem.getIdentMatchcode());
        dto.setModelType(bomItem.getModelType());
        dto.setSesamNumber(bomItem.getSesamNumber());
        dto.setMissingOnBoard(bomItem.getMissingOnBoard());
        dto.setQuantityOnBoard(bomItem.getQuantityOnBoard());
        dto.setObservation(bomItem.getObservation());
        dto.setPrice(bomItem.getPrice());
        return dto;
    }
}