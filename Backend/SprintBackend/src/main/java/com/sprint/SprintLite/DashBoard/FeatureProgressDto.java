package com.sprint.SprintLite.DashBoard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FeatureProgressDto {

    private Integer featureId;

    private String featureName;

    private Long totalStories;

    private Long completedStories;

    private Integer completion;

}