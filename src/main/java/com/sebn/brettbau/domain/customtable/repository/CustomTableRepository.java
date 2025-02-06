package com.sebn.brettbau.domain.customtable.repository;

import com.sebn.brettbau.domain.customtable.entity.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomTableRepository extends JpaRepository<CustomTable, Long> {
    List<CustomTable> findByCreatedBy(String createdBy);
}
