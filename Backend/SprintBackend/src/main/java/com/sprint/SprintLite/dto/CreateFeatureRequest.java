package com.sprint.SprintLite.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateFeatureRequest {
    String title;
    String description;
    Long productId;
    String Comments;
}
