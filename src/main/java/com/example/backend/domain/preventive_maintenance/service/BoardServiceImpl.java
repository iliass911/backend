// src/main/java/com/example/backend/domain/preventive_maintenance/service/BoardServiceImpl.java

package com.example.backend.domain.preventive_maintenance.service;

import com.example.backend.domain.preventive_maintenance.dto.BoardDTO;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import com.example.backend.domain.preventive_maintenance.entity.Pack;
import com.example.backend.domain.preventive_maintenance.mapper.BoardMapper;
import com.example.backend.domain.preventive_maintenance.repository.BoardRepository;
import com.example.backend.domain.preventive_maintenance.repository.PackRepository;
import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.user.repository.UserRepository;
import com.example.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BoardService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BoardMapper boardMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(boardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDTO getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        return boardMapper.toDTO(board);
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDTO) {
        Board board = boardMapper.toEntity(boardDTO);

        // Handle Pack relationship
        if (boardDTO.getPackId() != null) {
            Pack pack = packRepository.findById(boardDTO.getPackId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + boardDTO.getPackId()));
            board.setPack(pack);
        }

        // Handle User relationship
        if (boardDTO.getAssignedUserId() != null) {
            User user = userRepository.findById(boardDTO.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + boardDTO.getAssignedUserId()));
            board.setAssignedUser(user);
        }

        // Explicitly set all fields from DTO to ensure completeness
        board.setFbName(boardDTO.getFbName());
        board.setFbSize(boardDTO.getFbSize());
        board.setFirstTechLevel(boardDTO.getFirstTechLevel());
        board.setProjet(boardDTO.getProjet());
        board.setPlant(boardDTO.getPlant());
        board.setStoragePlace(boardDTO.getStoragePlace());
        board.setInUse(boardDTO.getInUse());
        board.setTestClip(boardDTO.getTestClip());
        board.setArea(boardDTO.getArea());
        board.setFbType1(boardDTO.getFbType1());
        board.setFbType2(boardDTO.getFbType2());
        board.setFbType3(boardDTO.getFbType3());
        board.setSide(boardDTO.getSide());
        board.setDerivate(boardDTO.getDerivate());
        board.setCreationReason(boardDTO.getCreationReason());
        board.setFirstYellowReleaseDate(boardDTO.getFirstYellowReleaseDate());
        board.setFirstOrangeReleaseDate(boardDTO.getFirstOrangeReleaseDate());
        board.setFirstGreenReleaseDate(boardDTO.getFirstGreenReleaseDate());
        board.setFirstUseByProdDate(boardDTO.getFirstUseByProdDate());
        board.setCurrentTechLevel(boardDTO.getCurrentTechLevel());
        board.setNextTechLevel(boardDTO.getNextTechLevel());
        board.setLastTechChangeImplemented(boardDTO.getLastTechChangeImplemented());
        board.setLastTechChangeImpleDate(boardDTO.getLastTechChangeImpleDate());
        board.setLastTechChangeReleaseDate(boardDTO.getLastTechChangeReleaseDate());
        board.setComment1(boardDTO.getComment1());
        board.setComment2(boardDTO.getComment2());
        board.setComment3(boardDTO.getComment3());
        board.setCost(boardDTO.getCost());
        board.setQuantity(boardDTO.getQuantity());
        board.setFbId(boardDTO.getFbId());

        Board savedBoard = boardRepository.save(board);
        return boardMapper.toDTO(savedBoard);
    }

    @Override
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

        // Update Pack relationship
        if (boardDTO.getPackId() != null) {
            Pack pack = packRepository.findById(boardDTO.getPackId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id: " + boardDTO.getPackId()));
            existingBoard.setPack(pack);
        } else {
            existingBoard.setPack(null);
        }

        // Update User relationship
        if (boardDTO.getAssignedUserId() != null) {
            User user = userRepository.findById(boardDTO.getAssignedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + boardDTO.getAssignedUserId()));
            existingBoard.setAssignedUser(user);
        } else {
            existingBoard.setAssignedUser(null);
        }

        // Explicitly update all fields from DTO to ensure consistency
        existingBoard.setFbName(boardDTO.getFbName());
        existingBoard.setFbSize(boardDTO.getFbSize());
        existingBoard.setFirstTechLevel(boardDTO.getFirstTechLevel());
        existingBoard.setProjet(boardDTO.getProjet());
        existingBoard.setPlant(boardDTO.getPlant());
        existingBoard.setStoragePlace(boardDTO.getStoragePlace());
        existingBoard.setInUse(boardDTO.getInUse());
        existingBoard.setTestClip(boardDTO.getTestClip());
        existingBoard.setArea(boardDTO.getArea());
        existingBoard.setFbType1(boardDTO.getFbType1());
        existingBoard.setFbType2(boardDTO.getFbType2());
        existingBoard.setFbType3(boardDTO.getFbType3());
        existingBoard.setSide(boardDTO.getSide());
        existingBoard.setDerivate(boardDTO.getDerivate());
        existingBoard.setCreationReason(boardDTO.getCreationReason());
        existingBoard.setFirstYellowReleaseDate(boardDTO.getFirstYellowReleaseDate());
        existingBoard.setFirstOrangeReleaseDate(boardDTO.getFirstOrangeReleaseDate());
        existingBoard.setFirstGreenReleaseDate(boardDTO.getFirstGreenReleaseDate());
        existingBoard.setFirstUseByProdDate(boardDTO.getFirstUseByProdDate());
        existingBoard.setCurrentTechLevel(boardDTO.getCurrentTechLevel());
        existingBoard.setNextTechLevel(boardDTO.getNextTechLevel());
        existingBoard.setLastTechChangeImplemented(boardDTO.getLastTechChangeImplemented());
        existingBoard.setLastTechChangeImpleDate(boardDTO.getLastTechChangeImpleDate());
        existingBoard.setLastTechChangeReleaseDate(boardDTO.getLastTechChangeReleaseDate());
        existingBoard.setComment1(boardDTO.getComment1());
        existingBoard.setComment2(boardDTO.getComment2());
        existingBoard.setComment3(boardDTO.getComment3());
        existingBoard.setCost(boardDTO.getCost());
        existingBoard.setQuantity(boardDTO.getQuantity());
        existingBoard.setFbId(boardDTO.getFbId());

        Board updatedBoard = boardRepository.save(existingBoard);
        return boardMapper.toDTO(updatedBoard);
    }

    @Override
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

        // Clear relationships before deleting
        board.setPack(null);
        board.setAssignedUser(null);

        boardRepository.delete(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctProjets() {
        return boardRepository.findDistinctProjets();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctPlants() {
        return boardRepository.findDistinctPlants();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctFbType1() {
        return boardRepository.findDistinctFbType1();
    }
}
