package com.sprint.SprintLite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_description")
    private  String description;
}
