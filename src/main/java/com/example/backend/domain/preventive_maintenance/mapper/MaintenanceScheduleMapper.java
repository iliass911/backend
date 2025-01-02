// src/main/java/com/example/backend/domain/preventive_maintenance/mapper/MaintenanceScheduleMapper.java

package com.example.backend.domain.preventive_maintenance.mapper;

import com.example.backend.domain.preventive_maintenance.dto.MaintenanceScheduleDTO;
import com.example.backend.domain.preventive_maintenance.entity.MaintenanceSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceScheduleMapper {

    MaintenanceScheduleDTO toDTO(MaintenanceSchedule schedule);

    MaintenanceSchedule toEntity(MaintenanceScheduleDTO scheduleDTO);
}
