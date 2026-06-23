package com.sprint.SprintLite.dto;

import lombok.Data;

@Data
public class CreateFeatureRequest {
    String title;
    String description;
    Long productId;
}
