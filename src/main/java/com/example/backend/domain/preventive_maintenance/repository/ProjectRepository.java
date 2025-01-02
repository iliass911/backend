package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findBySiteId(Long siteId);
    
    // Additional query methods (if needed) can be defined here
}
