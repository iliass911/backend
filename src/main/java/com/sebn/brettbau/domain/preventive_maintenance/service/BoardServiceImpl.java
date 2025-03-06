package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Pack;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.BoardMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.PackRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BoardService interface.
 */
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(boardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardDTO> getBoardsFiltered(String search, String projet, String plant, String fbType1) {
        // For simplicity, we just load all and filter in-memory.
        // In production, consider creating a JPA Specification or custom queries.

        List<Board> boards = boardRepository.findAll();
        return boards.stream()
            .filter(b -> {
                if (search != null && !search.isEmpty()) {
                    // example: match boardNumber or fbName
                    String s = search.toLowerCase();
                    boolean matchesName = (b.getFbName() != null && b.getFbName().toLowerCase().contains(s));
                    boolean matchesBoardNum = (b.getBoardNumber() != null && b.getBoardNumber().toLowerCase().contains(s));
                    if (!(matchesName || matchesBoardNum)) return false;
                }
                if (projet != null && !projet.isEmpty()) {
                    if (!projet.equals(b.getProjet())) return false;
                }
                if (plant != null && !plant.isEmpty()) {
                    if (!plant.equals(b.getPlant())) return false;
                }
                if (fbType1 != null && !fbType1.isEmpty()) {
                    if (!fbType1.equals(b.getFbType1())) return false;
                }
                return true;
            })
            .map(boardMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public BoardDTO getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        return boardMapper.toDTO(board);
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDTO) {
        // Optional check:
        // if (boardRepository.existsByBoardNumber(boardDTO.getBoardNumber())) {
        //     throw new IllegalArgumentException("Board Number already exists");
        // }

        Board board = boardMapper.toEntity(boardDTO);

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

        Board savedBoard = boardRepository.save(board);
        return boardMapper.toDTO(savedBoard);
    }

    @Override
    @Transactional
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

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

        Board savedBoard = boardRepository.save(existingBoard);
        return boardMapper.toDTO(savedBoard);
    }

    @Override
    public void deleteBoard(Long id) {
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        boardRepository.delete(existingBoard);
    }

    @Override
    public List<String> getDistinctProjets() {
        return boardRepository.findDistinctProjets();
    }

    @Override
    public List<String> getDistinctPlants() {
        return boardRepository.findDistinctPlants();
    }

    @Override
    public List<String> getDistinctFbType1() {
        return boardRepository.findDistinctFbType1();
    }

    @Override
    @Transactional
    public List<BoardDTO> createBulkBoards(List<BoardDTO> boardDTOs) {
        List<BoardDTO> createdBoards = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (BoardDTO dto : boardDTOs) {
            try {
                Board board = boardMapper.toEntity(dto);

                if (dto.getPackId() != null) {
                    Pack pack = packRepository.findById(dto.getPackId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Pack not found with id: " + dto.getPackId()));
                    board.setPack(pack);
                }

                if (dto.getAssignedUserId() != null) {
                    User user = userRepository.findById(dto.getAssignedUserId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "User not found with id: " + dto.getAssignedUserId()));
                    board.setAssignedUser(user);
                }

                Board savedBoard = boardRepository.save(board);
                createdBoards.add(boardMapper.toDTO(savedBoard));
            } catch (ResourceNotFoundException rnfe) {
                String errorMsg = "Row with boardNumber=" + dto.getBoardNumber()
                        + " failed: " + rnfe.getMessage();
                logger.error(errorMsg);
                errors.add(errorMsg);
            } catch (Exception ex) {
                String errorMsg = "Row with boardNumber=" + dto.getBoardNumber()
                        + " had an unexpected error: " + ex.getMessage();
                logger.error(errorMsg, ex);
                errors.add(errorMsg);
            }
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Errors occurred during bulk board creation: " + String.join("; ", errors));
        }

        return createdBoards;
    }

    // Example if you want a uniqueness check
    // @Override
    // public boolean existsBoardNumber(String boardNumber) {
    //     return boardRepository.existsByBoardNumber(boardNumber);
    // }
}
