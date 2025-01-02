package com.example.backend.domain.preventive_maintenance.service;

import com.example.backend.domain.preventive_maintenance.dto.ChecklistDTO;
import com.example.backend.domain.preventive_maintenance.dto.ProgressResponse;
import com.example.backend.domain.preventive_maintenance.entity.Board;
import com.example.backend.domain.preventive_maintenance.entity.Checklist;
import com.example.backend.domain.preventive_maintenance.mapper.ChecklistMapper;
import com.example.backend.domain.preventive_maintenance.repository.BoardRepository;
import com.example.backend.domain.preventive_maintenance.repository.ChecklistRepository;
import com.example.backend.domain.user.entity.User;
import com.example.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<ChecklistDTO> getAllChecklists() {
        return checklistRepository.findAll().stream()
                .map(ChecklistMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ChecklistDTO getChecklistById(Long id) {
        return checklistRepository.findById(id)
                .map(ChecklistMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
    }

    public ChecklistDTO createChecklist(ChecklistDTO dto) {
        validateCompletionPercentage(dto.getCompletionPercentage());
        
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));
        
        Checklist checklist = ChecklistMapper.toEntity(dto, board);
        checklist.setWorkStatus(determineWorkStatus(dto.getCompletionPercentage()));
        
        if (dto.getQualityValidated()) {
            checklist.setValidationDate(LocalDateTime.now());
            checklist.setExpiryDate(calculateNextScheduleDate());
        }
        
        return ChecklistMapper.toDTO(checklistRepository.save(checklist));
    }

    public void deleteChecklist(Long id) {
        checklistRepository.deleteById(id);
    }

    public ChecklistDTO updateChecklist(Long id, ChecklistDTO dto) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
        
        updateChecklistFields(checklist, dto);
        return ChecklistMapper.toDTO(checklistRepository.save(checklist));
    }

    private void updateChecklistFields(Checklist checklist, ChecklistDTO dto) {
        checklist.setTechnicianName(dto.getTechnicianName());
        checklist.setQualityAgentName(dto.getQualityAgentName());
        checklist.setCompletionPercentage(dto.getCompletionPercentage());
        checklist.setComments(dto.getComments());
        checklist.setQualityValidated(dto.getQualityValidated());
        checklist.setWorkStatus(determineWorkStatus(dto.getCompletionPercentage()));
        
        if (dto.getQualityValidated()) {
            checklist.setValidationDate(LocalDateTime.now());
            checklist.setExpiryDate(calculateNextScheduleDate());
        }
    }

    private String determineWorkStatus(Integer percentage) {
        if (percentage == null || percentage == 0) return "NOT_STARTED";
        if (percentage >= 100) return "COMPLETED";
        return "IN_PROGRESS";
    }

    private void validateCompletionPercentage(Integer percentage) {
        if (percentage == null) {
            throw new IllegalArgumentException("Completion percentage cannot be null");
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Completion percentage must be between 0 and 100");
        }
    }

    private LocalDateTime calculateNextScheduleDate() {
        return LocalDateTime.now().plusWeeks(1);
    }

    public ProgressResponse getUserProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Checklist> userChecklists = checklistRepository.findByBoardId(userId);
        long total = userChecklists.size();
        long completed = userChecklists.stream()
                .filter(c -> "COMPLETED".equals(c.getWorkStatus()))
                .count();
        
        return new ProgressResponse(total, completed, total - completed, 
                completed == total ? "Completed" : completed > (total/2) ? "Advanced" : "Retard");
    }
}