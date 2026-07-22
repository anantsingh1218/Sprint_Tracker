package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.BoardColumnDto;

import java.util.List;

public interface BoardService {

    List<BoardColumnDto>
    getBoard(
            Integer sprintId
    );

}