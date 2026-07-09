package com.sprint.SprintLite.dto;


import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private Integer taskCode;


    private String title;

    private String body;

    private String userCode;

    private String sprintCode;

    private String storyCode;

    private Status taskstatus;

    private Priority priority;

    private Integer originalestimatehours;

    private Integer remainingestimatehours;

    private String comments;

}
