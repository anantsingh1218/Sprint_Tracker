package com.sprint.SprintLite.DashBoard;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VelocityDto {
    private String sprintName;

    private Long completedStories;

    private Long totalStories;

    private Integer velocity;
}
