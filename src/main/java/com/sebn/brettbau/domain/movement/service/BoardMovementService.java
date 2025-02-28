package com.sebn.brettbau.domain.movement.service;

import com.sebn.brettbau.domain.movement.dto.BoardMovementDTO;
import com.sebn.brettbau.domain.movement.entity.BoardMovement;
import com.sebn.brettbau.domain.movement.repository.BoardMovementRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardMovementService {

    private final BoardMovementRepository repository;

    public List<BoardMovementDTO> getAllMovements() {
        return repository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public BoardMovementDTO getMovementById(Long id) {
        BoardMovement movement = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BoardMovement not found with id: " + id));
        return entityToDto(movement);
    }

    public BoardMovementDTO createOrUpdate(BoardMovementDTO dto) {
        BoardMovement entity = (dto.getId() != null)
                ? repository.findById(dto.getId()).orElse(new BoardMovement())
                : new BoardMovement();

        mapDtoToEntity(dto, entity);

        BoardMovement saved = repository.save(entity);
        return entityToDto(saved);
    }

    public void deleteMovement(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("BoardMovement not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private BoardMovementDTO entityToDto(BoardMovement e) {
        BoardMovementDTO dto = new BoardMovementDTO();
        dto.setId(e.getId());
        dto.setBoardId(e.getBoardId());
        dto.setFromLocation(e.getFromLocation());
        dto.setToLocation(e.getToLocation());
        dto.setMovedBy(e.getMovedBy());
        dto.setMoveDate(e.getMoveDate());
        dto.setReason(e.getReason());
        return dto;
    }

    private void mapDtoToEntity(BoardMovementDTO dto, BoardMovement e) {
        e.setBoardId(dto.getBoardId());
        e.setFromLocation(dto.getFromLocation());
        e.setToLocation(dto.getToLocation());
        e.setMovedBy(dto.getMovedBy());
        e.setMoveDate(dto.getMoveDate());
        e.setReason(dto.getReason());
    }
}
