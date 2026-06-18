package com.sprint.SprintLite.user.controller;

import com.sprint.SprintLite.dto.RegisterUserDto;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto){
        Users user = new Users();
        BeanUtils.copyProperties(registerUserDto, user);
        user.setPasswordhash(passwordEncoder.encode(registerUserDto.password()));
        user.setRole(registerUserDto.role());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Registered the User");
    }
}
