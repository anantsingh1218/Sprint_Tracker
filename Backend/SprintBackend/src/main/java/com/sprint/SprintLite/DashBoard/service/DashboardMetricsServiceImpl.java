package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.service.DashboardMetricsService;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor

public class DashboardMetricsServiceImpl
        implements DashboardMetricsService {

    private final
    TaskRepository
            taskRepository;

    @Override
    public Integer
    calculateProgress(

            Long total,

            Long completed

    ){

        if(
                total==0
        ){

            return 0;

        }

        return
                (
                        completed
                                .intValue()
                                *100
                )
                        /
                        total
                                .intValue();

    }


    @Override
    public Integer
    calculateCompletionRate(

            Long completed,

            Long total

    ){

        if(
                total==0
        ){

            return 0;

        }

        return
                (
                        completed
                                .intValue()
                                *100
                )
                        /
                        total
                                .intValue();

    }


    @Override
    public Integer
    calculateRemainingHours(

            Sprint sprint

    ){

        return

                taskRepository
                        .getRemainingHours(
                                sprint
                        );

    }

}