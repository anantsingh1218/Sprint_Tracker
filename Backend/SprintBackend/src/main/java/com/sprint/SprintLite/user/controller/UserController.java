package com.sprint.SprintLite.user.controller;

import com.sprint.SprintLite.dto.LoginRequest;
import com.sprint.SprintLite.dto.LoginResponseDto;
import com.sprint.SprintLite.dto.RegisterUserDto;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.UsersRepository;
import com.sprint.SprintLite.security.util.JwtUtil;
import com.sprint.SprintLite.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest request) {
        Users user = userRepository.findByUsername(request.username())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid credentials"
                        )
                );
        boolean isValid = passwordEncoder.matches(
                request.password(),
                user.getPasswordhash()
        );
        if (!isValid) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        String token = jwtUtil.generateJwtToken(authentication);
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
}
