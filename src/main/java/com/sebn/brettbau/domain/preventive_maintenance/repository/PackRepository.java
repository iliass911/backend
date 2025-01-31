// src/main/java/com/example/backend/domain/preventive_maintenance/repository/PackRepository.java
package com.sebn.brettbau.domain.preventive_maintenance.repository;

import com.sebn.brettbau.domain.preventive_maintenance.entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    List<Pack> findByProjectId(Long projectId);
    List<Pack> findBySiteId(Long siteId);
    List<Pack> findBySiteIdAndProjectId(Long siteId, Long projectId);
}
