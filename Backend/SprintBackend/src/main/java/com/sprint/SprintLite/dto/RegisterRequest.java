package com.sprint.SprintLite.dto;


import com.sprint.SprintLite.entity.enums.Role;
import lombok.Data;



@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private Role role;
}
