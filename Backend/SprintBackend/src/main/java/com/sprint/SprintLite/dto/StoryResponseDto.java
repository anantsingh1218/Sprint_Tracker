package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryResponseDto {
    private Integer id;
    private String title;
    private String body;
    private Integer featureId;
    private Integer userId;
    private Status storyStatus;
    private Priority priority;
    private Integer sprintId;
    private Integer storyPoints;
    private String comments;
}
