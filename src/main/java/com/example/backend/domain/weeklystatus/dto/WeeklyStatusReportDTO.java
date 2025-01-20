package com.example.backend.domain.weeklystatus.dto;

import lombok.Data;

@Data
public class WeeklyStatusReportDTO {
    private Long id;
    private String weekNumber;
    private String coordinator;
    private String responsiblePerson;
    private String projectCode;
    private String taskDescription;
    private String workType;
    private String priority;
    private Integer progressPercentage;
    private String project;
    private String month;
}
