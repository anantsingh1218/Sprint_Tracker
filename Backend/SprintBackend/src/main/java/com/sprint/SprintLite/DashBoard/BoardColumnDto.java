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

public class BoardColumnDto {

    private String status;

    private List<TaskCardDto> tasks;

}