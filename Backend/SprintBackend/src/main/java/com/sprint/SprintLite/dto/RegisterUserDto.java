package com.sprint.SprintLite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record RegisterUserDto(
        Long userId,

        @NotBlank(message = "Username is required")
        @Size(max = 255)
        String username,


        @NotBlank(message = "Email is required")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 12, max = 1023, message = "Password must be between 12 and 1023 characters")
        String password,

        @NotBlank(message = "Role is required")
        @Size(max = 50)
        String role,

        Instant createdAt,
        String createdBy
) {
}
