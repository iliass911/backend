package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT DISTINCT b.projet FROM Board b WHERE b.projet IS NOT NULL")
    List<String> findDistinctProjets();

    @Query("SELECT DISTINCT b.plant FROM Board b WHERE b.plant IS NOT NULL")
    List<String> findDistinctPlants();

    @Query("SELECT DISTINCT b.fbType1 FROM Board b WHERE b.fbType1 IS NOT NULL")
    List<String> findDistinctFbType1();
}