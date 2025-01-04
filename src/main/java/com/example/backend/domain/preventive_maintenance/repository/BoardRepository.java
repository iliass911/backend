package com.example.backend.domain.preventive_maintenance.repository;

import com.example.backend.domain.preventive_maintenance.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Board entity.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Checks if a board with the given boardNumber exists.
     *
     * @param boardNumber The board number to check.
     * @return true if a board with the boardNumber exists, false otherwise.
     */
    boolean existsByBoardNumber(String boardNumber);

    /**
     * Retrieves distinct projets from boards where projet is not null.
     *
     * @return List of distinct projets.
     */
    @Query("SELECT DISTINCT b.projet FROM Board b WHERE b.projet IS NOT NULL")
    List<String> findDistinctProjets();

    /**
     * Retrieves distinct plants from boards where plant is not null.
     *
     * @return List of distinct plants.
     */
    @Query("SELECT DISTINCT b.plant FROM Board b WHERE b.plant IS NOT NULL")
    List<String> findDistinctPlants();

    /**
     * Retrieves distinct fbType1 values from boards where fbType1 is not null.
     *
     * @return List of distinct fbType1 values.
     */
    @Query("SELECT DISTINCT b.fbType1 FROM Board b WHERE b.fbType1 IS NOT NULL")
    List<String> findDistinctFbType1();
}
