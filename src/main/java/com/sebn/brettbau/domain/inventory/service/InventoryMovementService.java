package com.sebn.brettbau.domain.inventory.service;

import com.sebn.brettbau.domain.inventory.dto.InventoryMovementDTO;
import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import com.sebn.brettbau.domain.inventory.entity.InventoryMovement;
import com.sebn.brettbau.domain.inventory.mapper.InventoryMovementMapper;
import com.sebn.brettbau.domain.inventory.repository.InventoryItemRepository;
import com.sebn.brettbau.domain.inventory.repository.InventoryMovementRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InventoryMovementService {

    private final InventoryMovementRepository movementRepository;
    private final InventoryItemRepository itemRepository;
    private final UserService userService;

    /**
     * Record a movement (change) in inventory stock
     * 
     * @param item The inventory item being changed
     * @param previousQuantity The quantity before the change
     * @param newQuantity The quantity after the change
     * @param reason The reason for the change (optional)
     */
    public void recordMovement(InventoryItem item, int previousQuantity, int newQuantity, String reason) {
        User currentUser = userService.getCurrentUser();
        
        int quantityChange = newQuantity - previousQuantity;
        String movementType = quantityChange > 0 ? "ADDITION" : "REMOVAL";
        
        // Create movement record
        InventoryMovement movement = InventoryMovement.builder()
                .item(item)
                .user(currentUser)
                .timestamp(LocalDateTime.now())
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .quantityChanged(Math.abs(quantityChange))
                .movementType(movementType)
                .reason(reason != null ? reason : "")
                .build();
                
        movementRepository.save(movement);
        log.info("Recorded inventory movement: Item {} quantity changed from {} to {} by user {}",
                item.getRefCode(), previousQuantity, newQuantity, currentUser.getUsername());
    }
    
    /**
     * Get all inventory movements with pagination
     */
    @Transactional(readOnly = true)
    public Page<InventoryMovementDTO> getAllMovements(Pageable pageable) {
        return movementRepository.findAll(pageable)
                .map(InventoryMovementMapper::toDTO);
    }
    
    /**
     * Get inventory movements for a specific item with pagination
     */
    @Transactional(readOnly = true)
    public Page<InventoryMovementDTO> getMovementsForItem(Long itemId, Pageable pageable) {
        return movementRepository.findByItemId(itemId, pageable)
                .map(InventoryMovementMapper::toDTO);
    }
    
    /**
     * Get inventory movements by site with pagination
     */
    @Transactional(readOnly = true)
    public Page<InventoryMovementDTO> getMovementsBySite(String site, Pageable pageable) {
        return movementRepository.findByItemSite(site, pageable)
                .map(InventoryMovementMapper::toDTO);
    }
    
    /**
     * Get inventory movements within a date range with pagination
     */
    @Transactional(readOnly = true)
    public Page<InventoryMovementDTO> getMovementsByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Pageable pageable) {
        return movementRepository.findByTimestampBetween(startDate, endDate, pageable)
                .map(InventoryMovementMapper::toDTO);
    }
}