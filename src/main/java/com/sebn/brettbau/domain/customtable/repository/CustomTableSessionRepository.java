package com.sebn.brettbau.domain.customtable.repository;

import com.sebn.brettbau.domain.customtable.entity.CustomTableSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomTableSessionRepository extends JpaRepository<CustomTableSession, Long> {
    List<CustomTableSession> findByTableIdAndIsActiveTrue(Long tableId);
    void deleteByUserIdAndTableId(String userId, Long tableId);
}
