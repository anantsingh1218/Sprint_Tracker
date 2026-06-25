package com.sprint.SprintLite.user.controller;

import com.sprint.SprintLite.dto.*;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.Role;
import com.sprint.SprintLite.repository.UsersRepository;
import com.sprint.SprintLite.security.util.JwtUtil;
import com.sprint.SprintLite.util.ApplicationUtility;
import com.sprint.SprintLite.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(
            @RequestBody RegisterUserDto registerUserDto) {

        boolean firstUser = userRepository.count() == 0;

        Users user = new Users();
        BeanUtils.copyProperties(registerUserDto, user);
        user.setPasswordhash(
                passwordEncoder.encode(registerUserDto.password()));

        if (firstUser) {

            user.setRole(Role.ROLE_PM);

        } else {

            Authentication auth =
                    SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            boolean allowed =
                    auth.getAuthorities().stream()
                            .anyMatch(a ->
                                    Objects.equals(a.getAuthority(), "ROLE_PM"));

            if (!allowed) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            user.setRole(registerUserDto.role());
        }

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponseDto(
                        "Successfully Registered the User"));
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

    @PatchMapping("/forgot-password")
    @Transactional
    public ResponseEntity<RegisterResponseDto> forgotPassword(@RequestBody ForgotPasswordDto request){
        Users user = userRepository.readUsersByEmailOrderByUsername(request.email(), request.username())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid credentials"
                        )
                );
        String randomPassword = PasswordGenerator.generateRandomPassword(15);
        user.setPasswordhash(passwordEncoder.encode(randomPassword));
        userRepository.save(user); //Performs update operation here cause id is found
        RegisterResponseDto registerResponseDto = new RegisterResponseDto("The updated password is: " + randomPassword);
        return ResponseEntity.ok(registerResponseDto);
    }
}
