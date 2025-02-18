package com.sebn.brettbau.domain.board.repository;

import com.sebn.brettbau.domain.board.entity.BoardFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardFamilyRepository extends JpaRepository<BoardFamily, Long> {
    List<BoardFamily> findByProject(String project);
    List<BoardFamily> findByProjectAndPhase(String project, String phase);
    
    @Query("SELECT DISTINCT b.project FROM BoardFamily b")
    List<String> findAllProjects();
    
    @Query("SELECT DISTINCT b.phase FROM BoardFamily b WHERE b.project = ?1")
    List<String> findPhasesByProject(String project);
    
    // New method to load a board family with its BOM items
    @Query("SELECT b FROM BoardFamily b LEFT JOIN FETCH b.bomItems WHERE b.id = ?1")
    BoardFamily findByIdWithBomItems(Long id);
}
