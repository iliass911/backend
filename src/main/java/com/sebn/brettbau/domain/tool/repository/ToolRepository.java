package com.sebn.brettbau.domain.tool.repository;

import com.sebn.brettbau.domain.tool.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    Tool findByToolId(String toolId);
}
