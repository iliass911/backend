// CustomTableRepository.java
package com.sebn.brettbau.domain.customtable.repository;

import com.sebn.brettbau.domain.customtable.entity.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTableRepository extends JpaRepository<CustomTable, Long> {
    // Remove findByCreatedBy to allow access to all tables
}