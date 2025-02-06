package com.sebn.brettbau.domain.customtable.repository;

import com.sebn.brettbau.domain.customtable.entity.CustomColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomColumnRepository extends JpaRepository<CustomColumn, Long> {
    List<CustomColumn> findByTableIdOrderByOrderIndexAsc(Long tableId);
}
