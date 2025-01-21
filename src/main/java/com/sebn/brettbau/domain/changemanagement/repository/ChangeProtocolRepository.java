// src/main/java/com/example/backend/domain/changemanagement/repository/ChangeProtocolRepository.java
package com.sebn.brettbau.domain.changemanagement.repository;

import com.sebn.brettbau.domain.changemanagement.entity.ChangeProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeProtocolRepository extends JpaRepository<ChangeProtocol, Long> {
    // Add custom queries if needed
}
