package com.sebn.brettbau.domain.preventive_maintenance.repository;

import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFamilyRepository extends JpaRepository<BoardFamily, Long> {
    // Optionally, add methods to check for existing family names
    boolean existsByFamilyName(String familyName);
}

