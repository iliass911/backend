package com.sebn.brettbau.domain.inventory.repository;

import com.sebn.brettbau.domain.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    // Custom queries if needed
}

