package com.sprint.SprintLite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDto {

    private Long assignedTasks;

    private Long completedTasks;

    private Long pendingTasks;

}