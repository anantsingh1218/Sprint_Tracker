//package com.sprint.SprintLite.worklog.Controller;
//
//import com.sprint.SprintLite.DashBoard.WorklogDto;
//import com.sprint.SprintLite.worklog.Service.WorklogService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//
//@RequestMapping(
//        "/worklog"
//)
//
//@RequiredArgsConstructor
//
//public class WorklogController {
//
//    private final
//    WorklogService
//            service;
//
//    @PostMapping
//
//    public void log(
//
//            @RequestBody
//            WorklogDto dto
//
//    ){
//
//        service.logHours(
//                dto
//        );
//
//    }
//
//    @GetMapping("/{taskId}")
//
//    public List<WorklogDto> logs(
//
//            @PathVariable
//            Integer taskId
//
//    ){
//
//        return service
//                .getLogs(
//                        taskId
//                );
//
//    }
//
//}