package com.example.backend.domain.weeklystatus.repository;

import com.example.backend.domain.weeklystatus.entity.WeeklyStatusReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyStatusReportRepository extends JpaRepository<WeeklyStatusReport, Long> {
    // Add custom queries if needed, for filtering, etc.
}
