package com.sprint.SprintLite.DashBoard;

import com.sprint.SprintLite.DashBoard.ProductSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PMDashboardDto {

    // Overall Dashboard

    private Integer totalProducts;

    private Integer activeSprints;

    private Long totalFeatures;

    private Long totalStories;

    private Long completedStories;

    private Long totalTasks;

    private Long completedTasks;

    private Long blockedTasks;

    private List<ProductSummaryDto> products;

}