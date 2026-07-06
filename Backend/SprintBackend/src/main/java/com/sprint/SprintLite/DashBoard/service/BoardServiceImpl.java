package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.BoardColumnDto;
import com.sprint.SprintLite.DashBoard.TaskCardDto;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.SprintRepository;
import com.sprint.SprintLite.repository.TaskRepository;

import com.sprint.SprintLite.repository.UsersRepository;
import com.sprint.SprintLite.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor

public class BoardServiceImpl
        implements BoardService {

    private final
    SprintRepository sprintRepository;
    private final UsersRepository usersRepository;

    private final TaskRepository taskRepository;

    @Override
    public List<BoardColumnDto> getBoard(Integer sprintId){

        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow();
        String userName= ApplicationUtility.getLoggedInUser();
        Users user = usersRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException(("User not found")));
        List<Task> tasks =
                taskRepository.findBySprintidAndUserid(
                        sprint,
                        user
                );

        return List.of(

                buildColumn("OPEN", tasks),

                buildColumn("TODO", tasks),

                buildColumn("IN_PROGRESS", tasks),

                buildColumn("DONE", tasks),

                buildColumn("BLOCKED", tasks)

        );

    }

    private BoardColumnDto
    buildColumn(String status, List<Task> tasks){

        return new BoardColumnDto(status, tasks.stream().filter(
                t -> t.getTaskstatus()!=null && t.getTaskstatus().name().equals(status))

                        .map(t -> new TaskCardDto(

                                        t.getId(),

                                        t.getTaskCode(),

                                        t.getTitle(),

                                        t.getPriority() != null
                                                ? t.getPriority().name()
                                                : "UNKNOWN",

                                        t.getStoryid() != null
                                                ? t.getStoryid().getTitle()
                                                : "-",

                                        t.getRemainingestimatehours(),

                                        t.getUserid() != null
                                                ? t.getUserid().getUsername()
                                                : "-"

                                )

                        )

                        .toList()

        );

    }

}