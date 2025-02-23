package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.ChecklistDTO;
import com.sebn.brettbau.domain.preventive_maintenance.dto.ProgressResponse;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Checklist;
import com.sebn.brettbau.domain.preventive_maintenance.entity.MaintenanceSchedule;
import com.sebn.brettbau.domain.preventive_maintenance.enums.BoardMaintenanceStatus;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.ChecklistMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.ChecklistRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.MaintenanceScheduleRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;  // Added repository dependency

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
     * Calculate maintenance status for a board based on schedule and checklists.
     */
    public BoardMaintenanceStatus calculateBoardStatus(Board board, int currentWeek, List<Integer> scheduledWeeks) {
        // Find closest scheduled week (past or future)
        Optional<Integer> nextScheduledWeek = scheduledWeeks.stream()
                .filter(week -> week >= currentWeek)
                .min(Integer::compareTo);

        Optional<Integer> previousScheduledWeek = scheduledWeeks.stream()
                .filter(week -> week < currentWeek)
                .max(Integer::compareTo);

        // Get latest checklist for the board
        Optional<Checklist> latestChecklist = checklistRepository
                .findTopByBoardOrderByCreatedAtDesc(board);

        if (latestChecklist.isEmpty()) {
            // No checklist exists - determine if we're behind schedule
            return previousScheduledWeek.isPresent() ? 
                   BoardMaintenanceStatus.RETARD : 
                   BoardMaintenanceStatus.PENDING;
        }

        Checklist checklist = latestChecklist.get();
        int checklistWeek = checklist.getWeekNumber();

        // If checklist is validated, check its timing
        if (checklist.getQualityValidated()) {
            if (nextScheduledWeek.isPresent() && checklistWeek < nextScheduledWeek.get()) {
                return BoardMaintenanceStatus.ADVANCED;
            } else if (previousScheduledWeek.isPresent() && checklistWeek <= previousScheduledWeek.get()) {
                return BoardMaintenanceStatus.COMPLETED;
            }
        }

        // Check if we're behind schedule
        if (previousScheduledWeek.isPresent() && checklistWeek <= previousScheduledWeek.get()
            && !checklist.getQualityValidated()) {
            return BoardMaintenanceStatus.RETARD;
        }

        // In progress but not yet due
        if (!checklist.getQualityValidated()) {
            return BoardMaintenanceStatus.IN_PROGRESS;
        }

        return BoardMaintenanceStatus.PENDING;
    }

    /**
     * Create a new checklist and update the associated Board status if necessary.
     */
    public ChecklistDTO createChecklist(ChecklistDTO dto) {
        validateCompletionPercentage(dto.getCompletionPercentage());

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // Ensure week number is provided
        if (dto.getWeekNumber() == null) {
            throw new IllegalArgumentException("Week number is required");
        }

        Checklist checklist = ChecklistMapper.toEntity(dto, board);

        // If quality validated, set validation date and expiry
        if (dto.getQualityValidated()) {
            checklist.setValidationDate(LocalDateTime.now());
            checklist.setExpiryDate(calculateNextMaintenanceDate(board));
            checklist.setWorkStatus("COMPLETED");
            board.setStatus("OK");
        } else {
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
            checklist.setExpiryDate(calculateNextMaintenanceDate(checklist.getBoard()));
        }
    }

    /**
     * Scheduled task to check and update board statuses daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void updateBoardStatuses() {
        List<Board> boards = boardRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Board board : boards) {
            Checklist latestChecklist = checklistRepository
                    .findTopByBoardOrderByValidationDateDesc(board)
                    .orElse(null);

            if (latestChecklist != null && latestChecklist.getExpiryDate() != null) {
                if (now.isAfter(latestChecklist.getExpiryDate())) {
                    board.setStatus("PENDING");
                    boardRepository.save(board);
                }
            }

            if ("PENDING".equals(board.getStatus()) && isPastScheduledWeek(board)) {
                board.setStatus("DANGER");
                boardRepository.save(board);
            }
        }
    }

    private boolean isPastScheduledWeek(Board board) {
        int currentWeek = LocalDateTime.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        return getCurrentScheduledWeek(board) < currentWeek;
    }

    /**
     * Updated calculateNextMaintenanceDate using MaintenanceScheduleRepository.
     */
    private LocalDateTime calculateNextMaintenanceDate(Board board) {
        // Get current week and year
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentWeek = now.get(WeekFields.ISO.weekOfWeekBasedYear());
        
        // Find next scheduled maintenance for this pack in current year
        Optional<MaintenanceSchedule> nextSchedule = maintenanceScheduleRepository
            .findFirstByPackIdAndYearAndWeekNumberGreaterThanOrderByWeekNumberAsc(
                board.getPack().getId(),
                currentYear,
                currentWeek
            );

        if (nextSchedule.isPresent()) {
            return now
                .with(WeekFields.ISO.weekOfWeekBasedYear(), nextSchedule.get().getWeekNumber())
                .with(WeekFields.ISO.dayOfWeek(), 1) // Start of week (Monday)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
        }

        // If no next schedule in current year, look for first schedule of next year
        List<MaintenanceSchedule> nextYearSchedules = maintenanceScheduleRepository
            .findByPackIdAndYearOrderByWeekNumberAsc(board.getPack().getId(), currentYear + 1);

        if (!nextYearSchedules.isEmpty()) {
            // Use first schedule of next year
            return now
                .plusYears(1)
                .with(WeekFields.ISO.weekOfWeekBasedYear(), nextYearSchedules.get(0).getWeekNumber())
                .with(WeekFields.ISO.dayOfWeek(), 1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
        }

        // If no schedules found at all, default to end of next week
        return now
            .plusWeeks(1)
            .with(WeekFields.ISO.dayOfWeek(), 7)
            .withHour(23)
            .withMinute(59)
            .withSecond(59);
    }

    private int findNextScheduledWeek(Board board, int currentWeek) {
        // TODO: Implement your logic to determine the next scheduled week for the given board.
        return currentWeek + 1; // Example placeholder
    }

    private int getCurrentScheduledWeek(Board board) {
        // TODO: Implement your logic based on board's schedule.
        return LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    private String determineWorkStatus(Integer percentage) {
        if (percentage == null || percentage == 0) {
            return "NOT_STARTED";
        }
        if (percentage >= 100) {
            return "COMPLETED";
        }
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

    /**
     * Get user progress including advanced and retard counts.
     */
    public ProgressResponse getUserProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use technician name instead of ID for fetching checklists
        List<Checklist> userChecklists = checklistRepository.findByTechnicianName(user.getUsername());

        int currentWeek = LocalDateTime.now().get(WeekFields.ISO.weekOfWeekBasedYear());

        long advanced = userChecklists.stream()
                .filter(cl -> cl.getQualityValidated() && cl.getWeekNumber() > currentWeek)
                .count();

        long retard = userChecklists.stream()
                .filter(cl -> !cl.getQualityValidated() && cl.getWeekNumber() < currentWeek)
                .count();

        long completed = userChecklists.stream()
                .filter(cl -> cl.getQualityValidated() && cl.getWeekNumber() <= currentWeek)
                .count();

        return new ProgressResponse(
            userChecklists.size(),
            completed,
            advanced,
            retard,
            advanced > retard ? "Advanced" : "Retard"
        );
    }
}
