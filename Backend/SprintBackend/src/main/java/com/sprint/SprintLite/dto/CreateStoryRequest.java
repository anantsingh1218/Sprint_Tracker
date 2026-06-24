package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.enums.Priority;
import com.sprint.SprintLite.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoryRequest {
private String title;
private String Body;
private Integer featureId;
private Integer userId;
private Status status;
private Priority priority;
private Integer sprintId;
private Integer storyPoints;
}
