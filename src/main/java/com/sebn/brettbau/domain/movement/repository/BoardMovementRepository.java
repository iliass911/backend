package com.sebn.brettbau.domain.movement.repository;

import com.sebn.brettbau.domain.movement.entity.BoardMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardMovementRepository extends JpaRepository<BoardMovement, Long> {
    // You can add custom queries here if needed.
}
