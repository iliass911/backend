package com.sebn.brettbau.domain.preventive_maintenance.repository;

import com.sebn.brettbau.domain.preventive_maintenance.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
}
