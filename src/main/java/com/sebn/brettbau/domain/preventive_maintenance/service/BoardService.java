package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import java.util.List;

/**
 * Service interface for managing Board entities.
 */
public interface BoardService {

    /**
     * Retrieves all Board entities and converts them to DTOs.
     *
     * @return List of BoardDTOs.
     */
    List<BoardDTO> getAllBoards();

    /**
     * Retrieves a Board by its ID and converts it to a DTO.
     *
     * @param id The ID of the Board.
     * @return BoardDTO representing the Board.
     * @throws ResourceNotFoundException if the Board is not found.
     */
    BoardDTO getBoardById(Long id);

    /**
     * Creates a new Board entity based on the provided DTO.
     *
     * @param boardDTO The BoardDTO containing Board details.
     * @return BoardDTO representing the created Board.
     * @throws ResourceNotFoundException if associated Pack or User is not found.
     */
    BoardDTO createBoard(BoardDTO boardDTO);

    /**
     * Updates an existing Board entity based on the provided DTO.
     *
     * @param id       The ID of the Board to update.
     * @param boardDTO The BoardDTO containing updated Board details.
     * @return BoardDTO representing the updated Board.
     * @throws ResourceNotFoundException if the Board is not found.
     */
    BoardDTO updateBoard(Long id, BoardDTO boardDTO);

    /**
     * Deletes a Board entity by its ID.
     *
     * @param id The ID of the Board to delete.
     * @throws ResourceNotFoundException if the Board is not found.
     */
    void deleteBoard(Long id);

    /**
     * Retrieves distinct project names from Boards.
     *
     * @return List of distinct project names.
     */
    List<String> getDistinctProjets();

    /**
     * Retrieves distinct plant names from Boards.
     *
     * @return List of distinct plant names.
     */
    List<String> getDistinctPlants();

    /**
     * Retrieves distinct FbType1 values from Boards.
     *
     * @return List of distinct FbType1 values.
     */
    List<String> getDistinctFbType1();

    /**
     * Creates multiple Board entities in bulk based on the provided DTOs.
     *
     * @param boardDTOs The list of BoardDTOs containing Board details.
     * @return List of BoardDTOs representing the created Boards.
     * @throws ResourceNotFoundException if associated resources are not found.
     */
    List<BoardDTO> createBulkBoards(List<BoardDTO> boardDTOs);
}

