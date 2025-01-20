package com.example.backend.domain.action.repository;

import com.example.backend.domain.action.entity.ActionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRecordRepository extends JpaRepository<ActionRecord, Long> {
    // Add custom queries if needed
}
