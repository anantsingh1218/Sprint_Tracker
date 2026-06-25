package com.sprint.SprintLite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseReadinessDto {

    private String productName;

    private String sprintName;

    private Integer completion;

    private Long openBugs;

    private Long blockedTasks;

    private Boolean releaseReady;

}