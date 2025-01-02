// BomRepository.java
package com.example.backend.domain.bom.repository;

import com.example.backend.domain.bom.entity.Bom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    Optional<Bom> findByBoardId(Long boardId);
}