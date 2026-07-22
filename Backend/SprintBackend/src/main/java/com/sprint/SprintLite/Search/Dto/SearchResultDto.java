package com.sprint.SprintLite.Search.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private Integer id;
    private String type;
    private String name;
}
