package com.sprint.SprintLite.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class CommentDto {
    private String userCode;
    private String text;
    private Instant createdAt;
}