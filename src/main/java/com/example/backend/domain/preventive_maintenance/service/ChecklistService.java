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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * Retrieve all checklists.
     */
    public List<ChecklistDTO> getAllChecklists() {
        return checklistRepository.findAll().stream()
                .map(ChecklistMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a specific checklist by its ID.
     */
    public ChecklistDTO getChecklistById(Long id) {
        return checklistRepository.findById(id)
                .map(ChecklistMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
    }

    /**
     * Create a new checklist and update the associated Board status if necessary.
     */
    public ChecklistDTO createChecklist(ChecklistDTO dto) {
        validateCompletionPercentage(dto.getCompletionPercentage());

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        Checklist checklist = ChecklistMapper.toEntity(dto, board);
        
        // If quality validated, set validation date and expiry
        if (dto.getQualityValidated()) {
            checklist.setValidationDate(LocalDateTime.now());
            checklist.setExpiryDate(calculateNextMaintenanceDate(board));
            checklist.setWorkStatus("COMPLETED");
            board.setStatus("OK");
        } else {
            // Not validated by quality -> IN_PROGRESS
            checklist.setWorkStatus("IN_PROGRESS");
            board.setStatus("IN_PROGRESS");
        }

        boardRepository.save(board);
        return ChecklistMapper.toDTO(checklistRepository.save(checklist));
    }

    /**
     * Update an existing checklist and associated Board status if needed.
     */
    public ChecklistDTO updateChecklist(Long id, ChecklistDTO dto) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        updateChecklistFields(checklist, dto);
        return ChecklistMapper.toDTO(checklistRepository.save(checklist));
    }

    /**
     * Delete a checklist by its ID.
     */
    public void deleteChecklist(Long id) {
        checklistRepository.deleteById(id);
    }

    /**
     * Utility method to copy fields from a DTO to an existing Checklist.
     */
    private void updateChecklistFields(Checklist checklist, ChecklistDTO dto) {
        validateCompletionPercentage(dto.getCompletionPercentage());

        checklist.setTechnicianName(dto.getTechnicianName());
        checklist.setQualityAgentName(dto.getQualityAgentName());
        checklist.setCompletionPercentage(dto.getCompletionPercentage());
        checklist.setComments(dto.getComments());
        checklist.setQualityValidated(dto.getQualityValidated());
        checklist.setWorkStatus(determineWorkStatus(dto.getCompletionPercentage()));

        if (dto.getQualityValidated()) {
            checklist.setValidationDate(LocalDateTime.now());
            // Example: next schedule date — adjust as needed
            checklist.setExpiryDate(calculateNextMaintenanceDate(checklist.getBoard()));
        }
    }

    /**
     * Scheduled task to check and update board statuses daily at midnight.
     *  - If a checklist has expired, the board is reset to "PENDING".
     *  - If a board is "PENDING" and we're past its scheduled week, it's set to "DANGER".
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void updateBoardStatuses() {
        List<Board> boards = boardRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Board board : boards) {
            // Find the latest checklist for this board
            Checklist latestChecklist = checklistRepository
                    .findTopByBoardOrderByValidationDateDesc(board)
                    .orElse(null);

            if (latestChecklist != null && latestChecklist.getExpiryDate() != null) {
                // If the checklist has expired, reset board status to PENDING
                if (now.isAfter(latestChecklist.getExpiryDate())) {
                    board.setStatus("PENDING");
                    boardRepository.save(board);
                }
            }

            // If board is PENDING and we're past the scheduled week, mark as DANGER
            if ("PENDING".equals(board.getStatus()) && isPastScheduledWeek(board)) {
                board.setStatus("DANGER");
                boardRepository.save(board);
            }
        }
    }

    /**
     * Determines if the current date is past the board's scheduled week.
     */
    private boolean isPastScheduledWeek(Board board) {
        int currentWeek = LocalDateTime.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        return getCurrentScheduledWeek(board) < currentWeek;
    }

    /**
     * Calculate the next maintenance date, e.g. the start of the next scheduled week.
     * You should adapt this logic based on your scheduling needs.
     */
    private LocalDateTime calculateNextMaintenanceDate(Board board) {
        int currentWeek = LocalDateTime.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        int nextScheduledWeek = findNextScheduledWeek(board, currentWeek);

        // Example: start of that next scheduled week (Monday)
        return LocalDateTime.now()
                .with(WeekFields.ISO.weekOfWeekBasedYear(), nextScheduledWeek)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Placeholder: implement your logic to find the next scheduled week for this Board.
     */
    private int findNextScheduledWeek(Board board, int currentWeek) {
        // TODO: Replace with your real logic. For illustration, let's just say next week.
        return currentWeek + 1;
    }

    /**
     * Placeholder: implement your logic to retrieve the Board's current scheduled week.
     */
    private int getCurrentScheduledWeek(Board board) {
        // TODO: Replace with your real logic. For illustration, let's return the current week.
        return LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    /**
     * Returns a work status string based on the completion percentage.
     */
    private String determineWorkStatus(Integer percentage) {
        if (percentage == null || percentage == 0) {
            return "NOT_STARTED";
        }
        if (percentage >= 100) {
            return "COMPLETED";
        }
        return "IN_PROGRESS";
    }

    /**
     * Validate the completion percentage is between 0 and 100 (inclusive).
     */
    private void validateCompletionPercentage(Integer percentage) {
        if (percentage == null) {
            throw new IllegalArgumentException("Completion percentage cannot be null");
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Completion percentage must be between 0 and 100");
        }
    }

    /**
     * Get the progress of a user by counting completed vs. total checklists (example logic).
     */
    public ProgressResponse getUserProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // NOTE: This assumes findByBoardId is actually retrieving checklists for that user,
        //       but that might need to be adjusted if your data model is different.
        List<Checklist> userChecklists = checklistRepository.findByBoardId(userId);
        long total = userChecklists.size();
        long completed = userChecklists.stream()
                .filter(c -> "COMPLETED".equals(c.getWorkStatus()))
                .count();

        return new ProgressResponse(
                total,
                completed,
                total - completed,
                completed == total
                        ? "Completed"
                        : (completed > total / 2 ? "Advanced" : "Retard")
        );
    }
}
