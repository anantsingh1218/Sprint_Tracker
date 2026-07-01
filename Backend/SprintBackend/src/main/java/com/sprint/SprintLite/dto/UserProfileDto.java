package com.sprint.SprintLite.dto;


import com.sprint.SprintLite.entity.enums.Role;
import lombok.Data;

@Data
public class UserProfileDto {
    private String username;
    private String email;
    private Role role;
}
