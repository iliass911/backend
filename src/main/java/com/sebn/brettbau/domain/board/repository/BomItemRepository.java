package com.sebn.brettbau.domain.board.repository;

import com.sebn.brettbau.domain.board.entity.BomItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BomItemRepository extends JpaRepository<BomItem, Long> {
    List<BomItem> findByBoardFamilyId(Long boardFamilyId);
}
