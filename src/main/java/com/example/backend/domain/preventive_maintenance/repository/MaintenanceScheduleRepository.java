// src/main/java/com/example/backend/domain/preventive_maintenance/repository/MaintenanceScheduleRepository.java

package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long> {
    Optional<MaintenanceSchedule> findBySiteIdAndPackIdAndWeekNumber(Long siteId, Long packId, Integer weekNumber);
}
