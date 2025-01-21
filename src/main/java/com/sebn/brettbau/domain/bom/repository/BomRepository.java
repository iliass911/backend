package com.sebn.brettbau.domain.bom.repository;

import com.sebn.brettbau.domain.bom.entity.Bom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    Optional<Bom> findByBoardId(Long boardId);
}
