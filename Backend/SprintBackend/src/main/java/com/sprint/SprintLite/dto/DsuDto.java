package com.sprint.SprintLite.dto;

import com.sprint.SprintLite.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class DsuDto {
    @NotNull(message = "Notes date is required")
    private LocalDate notesdate;

    @NotNull(message = "Status is required")
    private Status status;

    @NotBlank(message = "Completed work cannot be empty")
    private String completedwork;

    private String blockers;

    private String nextplan;
}
