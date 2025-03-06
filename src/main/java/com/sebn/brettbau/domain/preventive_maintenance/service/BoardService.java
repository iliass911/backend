package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import java.util.List;

/**
 * Service interface for managing Board entities.
 */
public interface BoardService {

    List<BoardDTO> getAllBoards(); // still exists if you need a simple fetch

    /**
     * Retrieves boards applying optional filters for search, projet, plant, fbType1, etc.
     */
    List<BoardDTO> getBoardsFiltered(String search, String projet, String plant, String fbType1);

    BoardDTO getBoardById(Long id);

    BoardDTO createBoard(BoardDTO boardDTO);

    BoardDTO updateBoard(Long id, BoardDTO boardDTO);

    void deleteBoard(Long id);

    List<String> getDistinctProjets();

    List<String> getDistinctPlants();

    List<String> getDistinctFbType1();

    List<BoardDTO> createBulkBoards(List<BoardDTO> boardDTOs);

    // If you want a uniqueness check for boardNumber
    // boolean existsBoardNumber(String boardNumber);
}
