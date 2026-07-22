package com.sprint.SprintLite.DashBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDto {

    private String productCode;

    private String productName;

    private String sprintName;

    private Integer progress;

    private Long totalFeatures;

    private Long totalStories;

    private Long completedStories;

    private Long totalTasks;

    private Long completedTasks;

    private Long pendingTasks;

    private Long blockedTasks;

}