package com.sprint.SprintLite.dto;


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
