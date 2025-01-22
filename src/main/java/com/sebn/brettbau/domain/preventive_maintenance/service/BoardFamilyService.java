package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.BoardFamilyMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardFamilyRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardFamilyService {
    private final BoardFamilyRepository familyRepository;
    private final BoardRepository boardRepository;
    private final BoardFamilyMapper familyMapper;

    public BoardFamilyService(
            BoardFamilyRepository familyRepository,
            BoardRepository boardRepository,
            BoardFamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.boardRepository = boardRepository;
        this.familyMapper = familyMapper;
    }

    public List<BoardFamilyDTO> getAllFamilies() {
        return familyRepository.findAll()
            .stream()
            .map(familyMapper::toDTO)
            .collect(Collectors.toList());
    }

    public BoardFamilyDTO createFamily(String familyName) {
        if (familyRepository.findByFamilyName(familyName).isPresent()) {
            throw new IllegalArgumentException("Family with name " + familyName + " already exists");
        }

        BoardFamily family = BoardFamily.builder()
                .familyName(familyName)
                .boardCount(0)
                .build();
        
        return familyMapper.toDTO(familyRepository.save(family));
    }

    @Transactional
    public BoardFamilyDTO addBoardToFamily(Long familyId, Long boardId) {
        BoardFamily family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found: " + familyId));
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        
        family.addBoard(board);
        return familyMapper.toDTO(familyRepository.save(family));
    }

    @Transactional
    public BoardFamilyDTO removeBoardFromFamily(Long familyId, Long boardId) {
        BoardFamily family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found: " + familyId));
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found: " + boardId));
        
        family.removeBoard(board);
        return familyMapper.toDTO(familyRepository.save(family));
    }

    @Transactional
    public void deleteFamily(Long familyId) {
        BoardFamily family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found: " + familyId));
        
        // Remove family reference from all boards
        family.getBoards().forEach(board -> board.setFamily(null));
        boardRepository.saveAll(family.getBoards());
        
        familyRepository.delete(family);
    }
}