package com.sebn.brettbau.domain.weeklystatus.service;

import com.sebn.brettbau.domain.weeklystatus.dto.WeeklyStatusReportDTO;
import com.sebn.brettbau.domain.weeklystatus.entity.WeeklyStatusReport;
import com.sebn.brettbau.domain.weeklystatus.repository.WeeklyStatusReportRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeeklyStatusReportService {
    private final WeeklyStatusReportRepository repository;

    private WeeklyStatusReportDTO mapToDTO(WeeklyStatusReport entity) {
        if (entity == null) return null;
        WeeklyStatusReportDTO dto = new WeeklyStatusReportDTO();
        dto.setId(entity.getId());
        dto.setWeekNumber(entity.getWeekNumber());
        dto.setCoordinator(entity.getCoordinator());
        dto.setResponsiblePerson(entity.getResponsiblePerson());
        dto.setProjectCode(entity.getProjectCode());
        dto.setTaskDescription(entity.getTaskDescription());
        dto.setWorkType(entity.getWorkType());
        dto.setPriority(entity.getPriority());
        dto.setProgressPercentage(entity.getProgressPercentage());
        dto.setProject(entity.getProject());
        dto.setMonth(entity.getMonth());
        return dto;
    }

    private WeeklyStatusReport mapToEntity(WeeklyStatusReportDTO dto) {
        if (dto == null) return null;
        return WeeklyStatusReport.builder()
                .id(dto.getId())
                .weekNumber(dto.getWeekNumber())
                .coordinator(dto.getCoordinator())
                .responsiblePerson(dto.getResponsiblePerson())
                .projectCode(dto.getProjectCode())
                .taskDescription(dto.getTaskDescription())
                .workType(dto.getWorkType())
                .priority(dto.getPriority())
                .progressPercentage(dto.getProgressPercentage())
                .project(dto.getProject())
                .month(dto.getMonth())
                .build();
    }

    public List<WeeklyStatusReportDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public WeeklyStatusReportDTO getById(Long id) {
        WeeklyStatusReport entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("WeeklyStatusReport not found with id: " + id));
        return mapToDTO(entity);
    }

    public WeeklyStatusReportDTO create(WeeklyStatusReportDTO dto) {
        WeeklyStatusReport entity = mapToEntity(dto);
        WeeklyStatusReport saved = repository.save(entity);
        return mapToDTO(saved);
    }

    public WeeklyStatusReportDTO update(Long id, WeeklyStatusReportDTO dto) {
        WeeklyStatusReport existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("WeeklyStatusReport not found with id: " + id));

        // Update fields
        existing.setWeekNumber(dto.getWeekNumber());
        existing.setCoordinator(dto.getCoordinator());
        existing.setResponsiblePerson(dto.getResponsiblePerson());
        existing.setProjectCode(dto.getProjectCode());
        existing.setTaskDescription(dto.getTaskDescription());
        existing.setWorkType(dto.getWorkType());
        existing.setPriority(dto.getPriority());
        existing.setProgressPercentage(dto.getProgressPercentage());
        existing.setProject(dto.getProject());
        existing.setMonth(dto.getMonth());

        WeeklyStatusReport updated = repository.save(existing);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("WeeklyStatusReport not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
