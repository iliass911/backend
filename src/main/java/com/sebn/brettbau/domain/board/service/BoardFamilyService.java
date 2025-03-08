package com.sebn.brettbau.domain.board.service;

import com.sebn.brettbau.domain.board.dto.BomComparisonItemDTO;
import com.sebn.brettbau.domain.board.dto.BomItemDTO;
import com.sebn.brettbau.domain.board.dto.BoardFamilyComparisonDTO;
import com.sebn.brettbau.domain.board.dto.BoardFamilyDTO;
import com.sebn.brettbau.domain.board.entity.BomItem;
import com.sebn.brettbau.domain.board.entity.BoardFamily;
import com.sebn.brettbau.domain.board.repository.BomItemRepository;
import com.sebn.brettbau.domain.board.repository.BoardFamilyRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardFamilyService {
    private final BoardFamilyRepository boardFamilyRepository;
    private final BomItemRepository bomItemRepository;

    @Transactional(readOnly = true)
    public List<BoardFamilyDTO> getAllBoardFamilies() {
        return boardFamilyRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardFamilyDTO getBoardFamilyById(Long id) {
        return boardFamilyRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Board family not found with id: " + id));
    }

    @Transactional
    public BoardFamilyDTO createBoardFamily(BoardFamilyDTO dto) {
        BoardFamily entity = mapToEntity(dto);
        BoardFamily saved = boardFamilyRepository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional
    public BoardFamilyDTO updateBoardFamily(Long id, BoardFamilyDTO dto) {
        BoardFamily existing = boardFamilyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Board family not found with id: " + id));

        updateEntityFromDTO(existing, dto);
        BoardFamily updated = boardFamilyRepository.save(existing);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteBoardFamily(Long id) {
        if (!boardFamilyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Board family not found with id: " + id);
        }
        boardFamilyRepository.deleteById(id);
    }

    @Transactional
    public BoardFamilyDTO duplicateBoardFamily(Long id, String newPhase) {
        BoardFamily original = boardFamilyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Board family not found with id: " + id));

        BoardFamily duplicate = new BoardFamily();
        duplicate.setFamilyName(original.getFamilyName() + " (Copy)");
        duplicate.setProject(original.getProject());
        duplicate.setSide(original.getSide());
        duplicate.setDerivate(original.getDerivate());
        duplicate.setNumberOfBoards(original.getNumberOfBoards());
        duplicate.setPhase(newPhase);

        // Save the duplicate first to get an ID
        BoardFamily savedDuplicate = boardFamilyRepository.save(duplicate);

        // Copy BOM items
        List<BomItem> duplicatedItems = original.getBomItems().stream()
            .map(item -> {
                BomItem newItem = new BomItem();
                newItem.setBoardFamily(savedDuplicate);
                newItem.setSegment(item.getSegment());
                newItem.setKurzname(item.getKurzname());
                newItem.setIdentMatchcode(item.getIdentMatchcode());
                newItem.setModelType(item.getModelType());
                newItem.setSesamNumber(item.getSesamNumber());
                newItem.setMissingOnBoard(item.getMissingOnBoard());
                newItem.setQuantityOnBoard(item.getQuantityOnBoard());
                newItem.setObservation(item.getObservation());
                newItem.setPrice(item.getPrice());
                newItem.setMotif(item.getMotif());
                return newItem;
            })
            .collect(Collectors.toList());

        savedDuplicate.setBomItems(duplicatedItems);
        BoardFamily finalSaved = boardFamilyRepository.save(savedDuplicate);
        return mapToDTO(finalSaved);
    }

    @Transactional(readOnly = true)
    public BoardFamilyComparisonDTO compareBoardFamilies(Long id1, Long id2) {
        BoardFamily family1 = boardFamilyRepository.findById(id1)
            .orElseThrow(() -> new ResourceNotFoundException("First board family not found"));
        BoardFamily family2 = boardFamilyRepository.findById(id2)
            .orElseThrow(() -> new ResourceNotFoundException("Second board family not found"));

        BoardFamilyComparisonDTO comparison = new BoardFamilyComparisonDTO();
        comparison.setFamily1(mapToDTO(family1));
        comparison.setFamily2(mapToDTO(family2));

        List<BomComparisonItemDTO> differences = compareBomItems(family1.getBomItems(), family2.getBomItems());
        comparison.setDifferences(differences);

        return comparison;
    }

    private List<BomComparisonItemDTO> compareBomItems(List<BomItem> items1, List<BomItem> items2) {
        List<BomComparisonItemDTO> differences = new ArrayList<>();
        Map<String, BomItem> items1Map = items1.stream()
            .collect(Collectors.toMap(BomItem::getIdentMatchcode, item -> item));
        Map<String, BomItem> items2Map = items2.stream()
            .collect(Collectors.toMap(BomItem::getIdentMatchcode, item -> item));

        // Check for modified and added items
        items2Map.forEach((matchcode, item2) -> {
            BomItem item1 = items1Map.get(matchcode);
            if (item1 == null) {
                differences.add(createComparisonItem(null, item2, "ADDED"));
            } else if (!areBomsEqual(item1, item2)) {
                differences.add(createComparisonItem(item1, item2, "MODIFIED"));
            }
        });

        // Check for removed items
        items1Map.forEach((matchcode, item1) -> {
            if (!items2Map.containsKey(matchcode)) {
                differences.add(createComparisonItem(item1, null, "REMOVED"));
            }
        });

        return differences;
    }

    private boolean areBomsEqual(BomItem item1, BomItem item2) {
        return Objects.equals(item1.getSegment(), item2.getSegment()) &&
               Objects.equals(item1.getKurzname(), item2.getKurzname()) &&
               Objects.equals(item1.getModelType(), item2.getModelType()) &&
               Objects.equals(item1.getSesamNumber(), item2.getSesamNumber()) &&
               Objects.equals(item1.getMissingOnBoard(), item2.getMissingOnBoard()) &&
               Objects.equals(item1.getQuantityOnBoard(), item2.getQuantityOnBoard()) &&
               Objects.equals(item1.getObservation(), item2.getObservation()) &&
               Objects.equals(item1.getPrice(), item2.getPrice()) &&
               Objects.equals(item1.getMotif(), item2.getMotif());
    }

    private BomComparisonItemDTO createComparisonItem(BomItem item1, BomItem item2, String type) {
        BomComparisonItemDTO dto = new BomComparisonItemDTO();
        dto.setItem1(item1 != null ? mapBomToDTO(item1) : null);
        dto.setItem2(item2 != null ? mapBomToDTO(item2) : null);
        dto.setComparisonType(type);

        if ("MODIFIED".equals(type)) {
            dto.setModifiedFields(findModifiedFields(item1, item2));
        }

        return dto;
    }

    private List<String> findModifiedFields(BomItem item1, BomItem item2) {
        List<String> modifiedFields = new ArrayList<>();
        if (!Objects.equals(item1.getSegment(), item2.getSegment()))
            modifiedFields.add("segment");
        if (!Objects.equals(item1.getKurzname(), item2.getKurzname()))
            modifiedFields.add("kurzname");
        if (!Objects.equals(item1.getModelType(), item2.getModelType()))
            modifiedFields.add("modelType");
        if (!Objects.equals(item1.getSesamNumber(), item2.getSesamNumber()))
            modifiedFields.add("sesamNumber");
        if (!Objects.equals(item1.getMissingOnBoard(), item2.getMissingOnBoard()))
            modifiedFields.add("missingOnBoard");
        if (!Objects.equals(item1.getQuantityOnBoard(), item2.getQuantityOnBoard()))
            modifiedFields.add("quantityOnBoard");
        if (!Objects.equals(item1.getObservation(), item2.getObservation()))
            modifiedFields.add("observation");
        if (!Objects.equals(item1.getPrice(), item2.getPrice()))
            modifiedFields.add("price");
        if (!Objects.equals(item1.getMotif(), item2.getMotif()))
            modifiedFields.add("motif");
        return modifiedFields;
    }

    private BoardFamilyDTO mapToDTO(BoardFamily entity) {
        if (entity == null) return null;

        BoardFamilyDTO dto = new BoardFamilyDTO();
        dto.setId(entity.getId());  // Ensure ID is set
        dto.setFamilyName(entity.getFamilyName());
        dto.setProject(entity.getProject());
        dto.setSide(entity.getSide());
        dto.setDerivate(entity.getDerivate());
        dto.setNumberOfBoards(entity.getNumberOfBoards());
        dto.setPhase(entity.getPhase());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getBomItems() != null) {
            dto.setBomItems(entity.getBomItems().stream()
                .map(this::mapBomToDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    private BomItemDTO mapBomToDTO(BomItem entity) {
        if (entity == null) return null;

        BomItemDTO dto = new BomItemDTO();
        dto.setId(entity.getId()); // Ensure ID is set
        dto.setSegment(entity.getSegment());
        dto.setKurzname(entity.getKurzname());
        dto.setIdentMatchcode(entity.getIdentMatchcode());
        dto.setModelType(entity.getModelType());
        dto.setSesamNumber(entity.getSesamNumber());
        dto.setMissingOnBoard(entity.getMissingOnBoard());
        dto.setQuantityOnBoard(entity.getQuantityOnBoard());
        dto.setObservation(entity.getObservation());
        dto.setPrice(entity.getPrice());
        dto.setMotif(entity.getMotif());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    private BoardFamily mapToEntity(BoardFamilyDTO dto) {
        if (dto == null) return null;

        BoardFamily entity = new BoardFamily();
        updateEntityFromDTO(entity, dto);
        return entity;
    }

    private void updateEntityFromDTO(BoardFamily entity, BoardFamilyDTO dto) {
        entity.setFamilyName(dto.getFamilyName());
        entity.setProject(dto.getProject());
        entity.setSide(dto.getSide());
        entity.setDerivate(dto.getDerivate());
        entity.setNumberOfBoards(dto.getNumberOfBoards());
        entity.setPhase(dto.getPhase());

        if (dto.getBomItems() != null) {
            List<BomItem> bomItems = dto.getBomItems().stream()
                .map(bomDto -> {
                    BomItem bomItem = bomDto.getId() != null
                        ? bomItemRepository.findById(bomDto.getId()).orElse(new BomItem())
                        : new BomItem();
                    updateBomItemFromDTO(bomItem, bomDto);
                    bomItem.setBoardFamily(entity);
                    return bomItem;
                })
                .collect(Collectors.toList());

            // Clear existing and replace
            entity.getBomItems().clear();
            entity.getBomItems().addAll(bomItems);
        }
    }

    private void updateBomItemFromDTO(BomItem entity, BomItemDTO dto) {
        entity.setSegment(dto.getSegment());
        entity.setKurzname(dto.getKurzname());
        entity.setIdentMatchcode(dto.getIdentMatchcode());
        entity.setModelType(dto.getModelType());
        entity.setSesamNumber(dto.getSesamNumber());
        entity.setMissingOnBoard(dto.getMissingOnBoard());
        entity.setQuantityOnBoard(dto.getQuantityOnBoard());
        entity.setObservation(dto.getObservation());
        entity.setPrice(dto.getPrice());
        entity.setMotif(dto.getMotif());
    }
}