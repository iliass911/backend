package com.sebn.brettbau.domain.board.service;

import com.sebn.brettbau.domain.board.dto.BomItemDTO;
import com.sebn.brettbau.domain.board.entity.BomItem;
import com.sebn.brettbau.domain.board.entity.BoardFamily;
import com.sebn.brettbau.domain.board.repository.BomItemRepository;
import com.sebn.brettbau.domain.board.repository.BoardFamilyRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BomItemService {

    private final BomItemRepository bomItemRepository;
    private final BoardFamilyRepository boardFamilyRepository;

    public BomItemService(BomItemRepository bomItemRepository, BoardFamilyRepository boardFamilyRepository) {
         this.bomItemRepository = bomItemRepository;
         this.boardFamilyRepository = boardFamilyRepository;
    }

    @Transactional
    public BomItemDTO createBomItem(BomItemDTO bomItemDTO) {
         BomItem bomItem = mapToEntity(bomItemDTO);
         BomItem saved = bomItemRepository.save(bomItem);
         return mapToDTO(saved);
    }

    @Transactional
    public BomItemDTO updateBomItem(Long id, BomItemDTO bomItemDTO) {
         BomItem existing = bomItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM item not found with id: " + id));
         updateEntity(existing, bomItemDTO);
         BomItem updated = bomItemRepository.save(existing);
         return mapToDTO(updated);
    }

    @Transactional
    public void deleteBomItem(Long id) {
         if (!bomItemRepository.existsById(id)) {
             throw new ResourceNotFoundException("BOM item not found with id: " + id);
         }
         bomItemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BomItemDTO getBomItemById(Long id) {
         BomItem bomItem = bomItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("BOM item not found with id: " + id));
         return mapToDTO(bomItem);
    }

    // Bulk creation endpoint: iterates through the list and reuses the single creation logic.
    @Transactional
    public List<BomItemDTO> bulkCreate(List<BomItemDTO> bomItemDTOs) {
        List<BomItemDTO> results = new ArrayList<>();
        for (BomItemDTO dto : bomItemDTOs) {
            BomItemDTO created = createBomItem(dto);
            results.add(created);
        }
        return results;
    }

    // --- Utility methods for mapping between DTO and entity ---

    private BomItemDTO mapToDTO(BomItem bomItem) {
         BomItemDTO dto = new BomItemDTO();
         dto.setId(bomItem.getId());
         dto.setSegment(bomItem.getSegment());
         dto.setKurzname(bomItem.getKurzname());
         dto.setIdentMatchcode(bomItem.getIdentMatchcode());
         dto.setModelType(bomItem.getModelType());
         dto.setSesamNumber(bomItem.getSesamNumber());
         dto.setMissingOnBoard(bomItem.getMissingOnBoard());
         dto.setQuantityOnBoard(bomItem.getQuantityOnBoard());
         dto.setObservation(bomItem.getObservation());
         dto.setPrice(bomItem.getPrice());
         dto.setMotif(bomItem.getMotif());
         dto.setCreatedAt(bomItem.getCreatedAt());
         dto.setUpdatedAt(bomItem.getUpdatedAt());
         if (bomItem.getBoardFamily() != null) {
            dto.setBoardFamilyId(bomItem.getBoardFamily().getId());
         }
         return dto;
    }

    private BomItem mapToEntity(BomItemDTO dto) {
         BomItem bomItem = new BomItem();
         bomItem.setSegment(dto.getSegment());
         bomItem.setKurzname(dto.getKurzname());
         bomItem.setIdentMatchcode(dto.getIdentMatchcode());
         bomItem.setModelType(dto.getModelType());
         bomItem.setSesamNumber(dto.getSesamNumber());
         bomItem.setMissingOnBoard(dto.getMissingOnBoard());
         bomItem.setQuantityOnBoard(dto.getQuantityOnBoard());
         bomItem.setObservation(dto.getObservation());
         bomItem.setPrice(dto.getPrice());
         bomItem.setMotif(dto.getMotif());
         // Look up and assign the BoardFamily if boardFamilyId is provided.
         if (dto.getBoardFamilyId() != null) {
             BoardFamily boardFamily = boardFamilyRepository.findById(dto.getBoardFamilyId())
                .orElseThrow(() -> new ResourceNotFoundException("Board family not found with id: " + dto.getBoardFamilyId()));
             bomItem.setBoardFamily(boardFamily);
         }
         return bomItem;
    }

    private void updateEntity(BomItem entity, BomItemDTO dto) {
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
         // Optionally update the BoardFamily association if boardFamilyId is provided.
         if (dto.getBoardFamilyId() != null) {
             BoardFamily boardFamily = boardFamilyRepository.findById(dto.getBoardFamilyId())
                .orElseThrow(() -> new ResourceNotFoundException("Board family not found with id: " + dto.getBoardFamilyId()));
             entity.setBoardFamily(boardFamily);
         }
    }
}