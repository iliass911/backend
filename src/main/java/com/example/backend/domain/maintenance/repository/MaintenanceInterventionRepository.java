package com.example.backend.domain.maintenance.repository;

import com.example.backend.domain.maintenance.entity.MaintenanceIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceInterventionRepository extends JpaRepository<MaintenanceIntervention, Long> {
    // Add custom queries if needed
}
