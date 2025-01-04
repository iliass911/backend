package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.Checklist;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByBoardId(Long boardId);

    Optional<Checklist> findTopByBoardOrderByValidationDateDesc(Board board);
}
