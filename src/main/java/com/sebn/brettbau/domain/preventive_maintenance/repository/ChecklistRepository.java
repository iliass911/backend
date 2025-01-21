package com.sebn.brettbau.domain.preventive_maintenance.repository;

import com.sebn.brettbau.domain.preventive_maintenance.entity.Checklist;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByBoardId(Long boardId);

    Optional<Checklist> findTopByBoardOrderByValidationDateDesc(Board board);
    
    Optional<Checklist> findTopByBoardOrderByCreatedAtDesc(Board board);
    
    List<Checklist> findByTechnicianName(String technicianName);
}
