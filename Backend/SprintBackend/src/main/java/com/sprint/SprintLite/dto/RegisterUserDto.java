package com.sprint.SprintLite.dto;

public record RegisterUserDto(
        String username,
        String email,
        String passwordHash,
        String role) {

}
