package com.sebn.brettbau.domain.tool.repository;

import com.sebn.brettbau.domain.tool.entity.ToolRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToolRequestRepository extends JpaRepository<ToolRequest, Long> {
    List<ToolRequest> findByRequestedBy(String requestedBy);
}
