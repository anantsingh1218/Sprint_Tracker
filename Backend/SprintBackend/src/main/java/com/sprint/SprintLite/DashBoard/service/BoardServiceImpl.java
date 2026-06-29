package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.BoardColumnDto;
import com.sprint.SprintLite.DashBoard.TaskCardDto;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.repository.SprintRepository;
import com.sprint.SprintLite.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor

public class BoardServiceImpl
        implements BoardService {

    private final
    SprintRepository
            sprintRepository;

    private final
    TaskRepository
            taskRepository;


    @Override
    public List<BoardColumnDto>
    getBoard(
            Integer sprintId
    ){

        Sprint sprint =

                sprintRepository
                        .findById(
                                sprintId
                        )
                        .orElseThrow();

        List<Task> tasks =

                taskRepository
                        .findBySprintid(
                                sprint
                        );

        return List.of(

                buildColumn(
                        "TODO",
                        tasks
                ),

                buildColumn(
                        "IN_PROGRESS",
                        tasks
                ),

                buildColumn(
                        "DONE",
                        tasks
                )

        );

    }


    private BoardColumnDto
    buildColumn(

            String status,

            List<Task> tasks

    ){

        return new BoardColumnDto(

                status,

                tasks

                        .stream()

                        .filter(

                                t ->

                                        t.getTaskstatus()!=null

                                                &&

                                                t.getTaskstatus()
                                                        .name()
                                                        .equals(
                                                                status
                                                        )

                        )

                        .map(

                                t ->

                                        new TaskCardDto(

                                                t.getId(),

                                                t.getTitle(),

                                                t.getPriority()!=null

                                                        ?

                                                        t.getPriority()
                                                        .name()

                                                        :

                                                        "UNKNOWN"

                                        )

                        )

                        .toList()

        );

    }

}