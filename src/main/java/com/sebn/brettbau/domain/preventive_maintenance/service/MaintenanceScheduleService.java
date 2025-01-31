package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.MaintenanceScheduleDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.MaintenanceSchedule;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.MaintenanceScheduleMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.MaintenanceScheduleRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceScheduleService {
    private final MaintenanceScheduleRepository scheduleRepository;
    private final MaintenanceScheduleMapper scheduleMapper;

    public MaintenanceScheduleService(MaintenanceScheduleRepository scheduleRepository,
                                      MaintenanceScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    public List<MaintenanceScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MaintenanceScheduleDTO createSchedule(MaintenanceScheduleDTO dto) {
        scheduleRepository.findByProjectIdAndPackIdAndWeekNumber(
                dto.getProjectId(), dto.getPackId(), dto.getWeekNumber())
                .ifPresent(schedule -> {
                    throw new IllegalArgumentException(
                        "Schedule already exists for this Project, Pack, and Week.");
                });

        MaintenanceSchedule schedule = scheduleMapper.toEntity(dto);
        MaintenanceSchedule saved = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(saved);
    }

    public void deleteSchedule(Long projectId, Long packId, Integer weekNumber) {
        MaintenanceSchedule schedule = scheduleRepository
                .findByProjectIdAndPackIdAndWeekNumber(projectId, packId, weekNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance schedule not found."));
        scheduleRepository.delete(schedule);
    }
}

