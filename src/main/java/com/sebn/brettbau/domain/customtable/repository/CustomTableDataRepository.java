package com.sebn.brettbau.domain.customtable.repository;

import com.sebn.brettbau.domain.customtable.entity.CustomTableData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomTableDataRepository extends JpaRepository<CustomTableData, Long> {
    @Query("SELECT d FROM CustomTableData d WHERE d.table.id = ?1 ORDER BY d.rowIndex, d.column.orderIndex")
    List<CustomTableData> findByTableIdOrdered(Long tableId);

    void deleteByTableIdAndRowIndex(Long tableId, Integer rowIndex);

    @Query("SELECT d FROM CustomTableData d WHERE d.table.id = ?1 AND d.column.id = ?2 AND d.rowIndex = ?3")
    Optional<CustomTableData> findByTableIdAndColumnIdAndRowIndex(Long tableId, Long columnId, Integer rowIndex);

    // New method to delete all data for a specific table
    @Modifying
    @Query("DELETE FROM CustomTableData d WHERE d.table.id = :tableId")
    void deleteByTableId(@Param("tableId") Long tableId);

    // Additional method to fetch all data for a specific table
    @Query("SELECT d FROM CustomTableData d WHERE d.table.id = :tableId ORDER BY d.rowIndex, d.column.orderIndex")
    List<CustomTableData> findAllByTableId(@Param("tableId") Long tableId);
}