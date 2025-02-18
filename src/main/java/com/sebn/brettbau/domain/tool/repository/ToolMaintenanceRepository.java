package com.sebn.brettbau.domain.tool.repository;

import com.sebn.brettbau.domain.tool.entity.ToolMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToolMaintenanceRepository extends JpaRepository<ToolMaintenance, Long> {
    List<ToolMaintenance> findByToolId(Long toolId);
}
