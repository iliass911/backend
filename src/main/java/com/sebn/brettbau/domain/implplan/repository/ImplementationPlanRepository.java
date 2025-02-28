package com.sebn.brettbau.domain.implplan.repository;

import com.sebn.brettbau.domain.implplan.entity.ImplementationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImplementationPlanRepository extends JpaRepository<ImplementationPlan, Long> {
    // Additional custom queries if needed
}
