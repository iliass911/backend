package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.BoardFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardFamilyRepository extends JpaRepository<BoardFamily, Long> {
    Optional<BoardFamily> findByProjetAndSideAndFbType2AndFbType3AndFbSizeAndDerivate(
        String projet,
        String side,
        String fbType2,
        String fbType3,
        String fbSize,
        String derivate
    );
}
