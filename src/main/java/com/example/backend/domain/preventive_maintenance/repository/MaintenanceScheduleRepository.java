// src/main/java/com/example/backend/domain/preventive_maintenance/repository/MaintenanceScheduleRepository.java
package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long> {
    Optional<MaintenanceSchedule> findByProjectIdAndPackIdAndWeekNumber(Long projectId, Long packId, Integer weekNumber);
    
    List<MaintenanceSchedule> findByProjectId(Long projectId);
    
    List<MaintenanceSchedule> findByPackIdOrderByWeekNumberAsc(Long packId);
    
    List<MaintenanceSchedule> findByPackIdAndYearOrderByWeekNumberAsc(Long packId, Integer year);
    
    Optional<MaintenanceSchedule> findFirstByPackIdAndYearAndWeekNumberGreaterThanOrderByWeekNumberAsc(
            Long packId, 
            Integer year, 
            Integer weekNumber
    );
}