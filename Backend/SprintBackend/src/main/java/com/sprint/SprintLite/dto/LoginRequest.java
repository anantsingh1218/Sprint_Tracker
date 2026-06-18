package com.sprint.SprintLite.dto;

import lombok.Data;

//@Data
//public class LoginRequest {
//    private String email;
//    private String password;
//}
public record LoginRequest (
        String username,
        String password
){

}

