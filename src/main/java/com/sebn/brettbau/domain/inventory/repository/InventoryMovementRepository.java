package com.sebn.brettbau.domain.inventory.repository;

import com.sebn.brettbau.domain.inventory.entity.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    
    Page<InventoryMovement> findByItemId(Long itemId, Pageable pageable);
    
    @Query("SELECT m FROM InventoryMovement m JOIN m.item i WHERE i.site = :site")
    Page<InventoryMovement> findByItemSite(@Param("site") String site, Pageable pageable);
    
    Page<InventoryMovement> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<InventoryMovement> findByUserId(Long userId, Pageable pageable);
}