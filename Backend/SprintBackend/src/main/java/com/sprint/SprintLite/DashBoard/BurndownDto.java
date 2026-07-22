package com.sprint.SprintLite.DashBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BurndownDto {

    private String sprintName;

    private List<String> labels;

    private List<Integer> actual;

    private List<Integer> ideal;

}