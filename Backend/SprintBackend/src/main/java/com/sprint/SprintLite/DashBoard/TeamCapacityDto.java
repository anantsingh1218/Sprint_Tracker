package com.sprint.SprintLite.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamCapacityDto {

    private Integer totalMembers;

    private Integer availableMembers;

    private Integer busyMembers;

    private Integer overloadedMembers;

}