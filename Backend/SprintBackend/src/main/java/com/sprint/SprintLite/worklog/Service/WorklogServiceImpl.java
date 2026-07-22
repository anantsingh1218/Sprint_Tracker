//package com.sprint.SprintLite.worklog.Service;
//
//import com.sprint.SprintLite.DashBoard.WorklogDto;
//import com.sprint.SprintLite.entity.Task;
//import com.sprint.SprintLite.entity.Worklog;
//import com.sprint.SprintLite.repository.TaskRepository;
//import com.sprint.SprintLite.repository.WorklogRepository;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class WorklogServiceImpl
//        implements WorklogService {
//
//    private final
//    WorklogRepository
//            worklogRepository;
//
//    private final
//    TaskRepository
//            taskRepository;
//
//    @Override
//    public void logHours(
//            WorklogDto dto
//    ) {
//
//        Task task =
//
//                taskRepository
//                        .findById(
//                                dto.getTaskId()
//                        )
//                        .orElseThrow(
//                                () ->
//                                        new RuntimeException(
//                                                "Task not found"
//                                        )
//                        );
//
//        Worklog log =
//                new Worklog();
//
//        log.setTaskid(
//                task
//        );
//
//        log.setHoursspent(
//                BigDecimal.valueOf(
//                        dto.getHours()
//                )
//        );
//
//        log.setRemarks(
//                dto.getComment()
//        );
//
//        log.setWorkdate(
//                LocalDate.now()
//        );
//
//        if (
//                task.getRemainingestimatehours() != null
//        ) {
//
//            task.setRemainingestimatehours(
//
//                    Math.max(
//                            0,
//
//                            task.getRemainingestimatehours()
//
//                                    -
//
//                                    dto.getHours()
//                    )
//
//            );
//
//            taskRepository
//                    .save(
//                            task
//                    );
//
//        }
//
//    }
//
//    @Override
//    public List<WorklogDto>
//    getLogs(
//            Integer taskId
//    ) {
//
//        Task task =
//                taskRepository
//                        .findById(taskId)
//                        .orElseThrow();
//
//        return worklogRepository
//                .findByTaskid(task)
//
//                .stream()
//
//                .map(
//
//                        w ->
//
//                                new WorklogDto(
//
//                                        taskId,
//
//                                        w.getHoursspent()
//                                                .intValue(),
//
//                                        w.getRemarks()
//
//                                )
//
//                )
//
//                .toList();
//
//    }
//}