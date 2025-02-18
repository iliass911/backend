package com.sebn.brettbau.domain.tool.repository;

import com.sebn.brettbau.domain.tool.entity.ToolTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToolTransferRepository extends JpaRepository<ToolTransfer, Long> {
    List<ToolTransfer> findByFromUserOrToUser(String fromUser, String toUser);
}
