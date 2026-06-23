package com.sprint.SprintLite.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AttachmentDto(
        Integer id,
        String filename,
        @JsonIgnore String filepath // Visible inside the backend, but hidden from the JSON response
) {

}