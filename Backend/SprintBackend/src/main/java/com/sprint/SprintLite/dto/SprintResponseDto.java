package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.SprintStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponseDto {
    private Integer id;
    private String sprintName;
    private String productCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer sprintDuration;
    private SprintStatus status;


}