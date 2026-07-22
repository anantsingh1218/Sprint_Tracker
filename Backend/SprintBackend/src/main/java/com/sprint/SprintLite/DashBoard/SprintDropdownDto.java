package com.sprint.SprintLite.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintDropdownDto {

    private Integer id;          // Internal

    private String sprintCode;   // Display

    private String sprintName;

}