package com.sprint.SprintLite.worklog.Service;

import com.sprint.SprintLite.DashBoard.WorklogDto;

import java.util.List;

public interface WorklogService {

    void logHours(
            WorklogDto dto
    );

    List<WorklogDto> getLogs(
            Integer taskId
    );

}