package com.sebn.brettbau.domain.kpis.repository;

import com.sebn.brettbau.domain.kpis.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
