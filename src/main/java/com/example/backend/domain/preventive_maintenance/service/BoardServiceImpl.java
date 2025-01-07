package com.example.backend.domain.preventive_maintenance.service;

import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import com.example.backend.domain.preventive_maintenance.entity.Pack;
import com.example.backend.domain.user.entity.User;  // Correct import for User
import com.example.backend.domain.preventive_maintenance.mapper.BoardMapper;
import com.example.backend.domain.preventive_maintenance.repository.BoardRepository;
import com.example.backend.domain.preventive_maintenance.repository.PackRepository;
import com.example.backend.domain.user.repository.UserRepository;  // Correct import for UserRepository
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    
    // Autowired repositories and mappers
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    // Repositories for handling Pack and User relationships
    @Autowired
    private PackRepository packRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BoardDTO> getAllBoards() {
        try {
            System.out.println("Fetching all boards from the repository.");
            List<Board> boards = boardRepository.findAll();
            List<BoardDTO> boardDTOs = boards.stream()
                    .map(boardMapper::toDTO)
                    .collect(Collectors.toList());
            System.out.println("Fetched " + boardDTOs.size() + " boards.");
            return boardDTOs;
        } catch (Exception e) {
            System.out.println("Error in getAllBoards: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public BoardDTO getBoardById(Long id) {
        try {
            System.out.println("Fetching board with id: " + id);
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
            BoardDTO boardDTO = boardMapper.toDTO(board);
            System.out.println("Fetched board: " + boardDTO);
            return boardDTO;
        } catch (ResourceNotFoundException e) {
            System.out.println("Board not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error in getBoardById: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDTO) {
        try {
            // Log incoming DTO
            System.out.println("Creating board with data: " + boardDTO);

            // Map to entity
            Board board = boardMapper.toEntity(boardDTO);
            System.out.println("Mapped to entity: " + board);

            // Handle Pack relationship
            if (boardDTO.getPackId() != null) {
                Pack pack = packRepository.findById(boardDTO.getPackId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Pack not found with id: " + boardDTO.getPackId()));
                board.setPack(pack);
            }

            // Handle User relationship
            if (boardDTO.getAssignedUserId() != null) {
                User user = userRepository.findById(boardDTO.getAssignedUserId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "User not found with id: " + boardDTO.getAssignedUserId()));
                board.setAssignedUser(user);
            }

            // Save entity
            Board savedBoard = boardRepository.save(board);
            System.out.println("Saved board: " + savedBoard);

            // Map back to DTO
            BoardDTO result = boardMapper.toDTO(savedBoard);
            System.out.println("Returning DTO: " + result);

            return result;
        } catch (Exception e) {
            System.out.println("Error in createBoard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Updates an existing Board entity by merging changes from a BoardDTO,
     * and also manages the Pack and User relationships if present.
     */
    @Override
    @Transactional
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        try {
            System.out.println("Updating board with id: " + id + " and data: " + boardDTO);

            // Retrieve existing board
            Board existingBoard = boardRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

            // Update the entity using mapper while preserving certain fields
            boardMapper.updateEntityFromDto(boardDTO, existingBoard);

            // Handle pack relationship
            if (boardDTO.getPackId() != null) {
                Pack pack = packRepository.findById(boardDTO.getPackId())
                        .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + boardDTO.getPackId()));
                existingBoard.setPack(pack);
            } else {
                existingBoard.setPack(null);
            }

            // Handle user relationship
            if (boardDTO.getAssignedUserId() != null) {
                User user = userRepository.findById(boardDTO.getAssignedUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + boardDTO.getAssignedUserId()));
                existingBoard.setAssignedUser(user);
            } else {
                existingBoard.setAssignedUser(null);
            }

            // Save the updated board
            Board savedBoard = boardRepository.save(existingBoard);
            System.out.println("Saved board with pack: " + savedBoard.getPack());

            // Map back to DTO
            BoardDTO result = boardMapper.toDTO(savedBoard);
            System.out.println("Returning updated DTO with pack_id: " + result.getPackId());

            return result;
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found during update: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error in updateBoard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteBoard(Long id) {
        try {
            System.out.println("Deleting board with id: " + id);
            Board existingBoard = boardRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
            boardRepository.delete(existingBoard);
            System.out.println("Successfully deleted board with id: " + id);
        } catch (ResourceNotFoundException e) {
            System.out.println("Board not found during deletion: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error in deleteBoard: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<String> getDistinctProjets() {
        try {
            System.out.println("Fetching distinct projets.");
            List<String> projets = boardRepository.findDistinctProjets();
            System.out.println("Fetched " + projets.size() + " distinct projets.");
            return projets;
        } catch (Exception e) {
            System.out.println("Error in getDistinctProjets: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<String> getDistinctPlants() {
        try {
            System.out.println("Fetching distinct plants.");
            List<String> plants = boardRepository.findDistinctPlants();
            System.out.println("Fetched " + plants.size() + " distinct plants.");
            return plants;
        } catch (Exception e) {
            System.out.println("Error in getDistinctPlants: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<String> getDistinctFbType1() {
        try {
            System.out.println("Fetching distinct fbType1 values.");
            List<String> fbTypes1 = boardRepository.findDistinctFbType1();
            System.out.println("Fetched " + fbTypes1.size() + " distinct fbType1 values.");
            return fbTypes1;
        } catch (Exception e) {
            System.out.println("Error in getDistinctFbType1: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Creates multiple Board entities in bulk based on the provided DTOs.
     *
     * @param boardDTOs The list of BoardDTOs containing Board details.
     * @return List of BoardDTOs representing the created Boards.
     * @throws RuntimeException if any board fails to process.
     */
    @Override
    @Transactional
    public List<BoardDTO> createBulkBoards(List<BoardDTO> boardDTOs) {
        List<BoardDTO> createdBoards = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Process all boards
        for (BoardDTO boardDTO : boardDTOs) {
            try {
                // Map DTO to entity
                Board board = boardMapper.toEntity(boardDTO);
                System.out.println("Mapped BoardDTO to Board entity: " + board);

                // Handle Pack relationship
                if (boardDTO.getPackId() != null) {
                    Pack pack = packRepository.findById(boardDTO.getPackId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Pack not found with id: " + boardDTO.getPackId()));
                    board.setPack(pack);
                    System.out.println("Set Pack for Board: " + pack);
                }

                // Handle User relationship
                if (boardDTO.getAssignedUserId() != null) {
                    User user = userRepository.findById(boardDTO.getAssignedUserId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "User not found with id: " + boardDTO.getAssignedUserId()));
                    board.setAssignedUser(user);
                    System.out.println("Set AssignedUser for Board: " + user);
                }

                // Save entity
                Board savedBoard = boardRepository.save(board);
                System.out.println("Saved Board: " + savedBoard);

                // Map back to DTO and add to the result list
                BoardDTO savedBoardDTO = boardMapper.toDTO(savedBoard);
                createdBoards.add(savedBoardDTO);
                System.out.println("Added saved BoardDTO to createdBoards list: " + savedBoardDTO);

            } catch (ResourceNotFoundException rnfe) {
                String errorMsg = "Resource not found for board number: " + boardDTO.getBoardNumber() + ". " + rnfe.getMessage();
                System.err.println(errorMsg);
                errors.add(errorMsg);
                // Optionally, you can continue processing other boards instead of throwing immediately
                // Uncomment the next line if you want to stop processing on the first error
                // throw new RuntimeException(errorMsg, rnfe);
            } catch (Exception e) {
                String errorMsg = "Error processing board number: " + boardDTO.getBoardNumber();
                System.err.println(errorMsg);
                e.printStackTrace();
                errors.add(errorMsg);
                // Optionally, you can continue processing other boards instead of throwing immediately
                // Uncomment the next line if you want to stop processing on the first error
                // throw new RuntimeException(errorMsg, e);
            }
        }

        if (!errors.isEmpty()) {
            // You can choose how to handle errors. Here, we're throwing an exception with all error messages.
            throw new RuntimeException("Errors occurred during bulk board creation: " + String.join("; ", errors));
        }

        return createdBoards;
    }
}
