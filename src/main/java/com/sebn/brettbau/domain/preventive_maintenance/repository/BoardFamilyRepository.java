package com.sebn.brettbau.domain.preventive_maintenance.repository;

import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardFamilyRepository extends JpaRepository<BoardFamily, Long> {
    Optional<BoardFamily> findByFamilyName(String familyName);
}