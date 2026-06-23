package com.sprint.SprintLite.user.controller;

import com.sprint.SprintLite.dto.LoginRequest;
import com.sprint.SprintLite.dto.RegisterUserDto;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.UsersRepository;
import com.sprint.SprintLite.security.util.JwtUtil;
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

   /* @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Users user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        ()->new RuntimeException("User not found (Not Found from SQL Query)")
                );
        System.out.println(user.getUsername());
        boolean isValid=passwordEncoder.matches(request.getPassword(), user.getPasswordhash());
        if(!isValid){
            throw new RuntimeException("Incorrect password");
        }
        var resultAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return ResponseEntity.ok().body(jwtUtil.generateJwtToken(resultAuthentication));
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("===== LOGIN REQUEST RECEIVED =====");
        System.out.println("Email: " + request.username());

        Users user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> {
                    System.out.println("User NOT found in database");
                    return new RuntimeException("User not found (Not Found from SQL Query)");
                });

        System.out.println("User found in database");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());

        boolean isValid =
                passwordEncoder.matches(
                        request.password(),
                        user.getPasswordhash()
                );

        System.out.println("Password match result: " + isValid);

        if (!isValid) {
            System.out.println("Password validation failed");
            throw new RuntimeException("Incorrect password");
        }

        System.out.println("Calling AuthenticationManager...");

        Authentication resultAuthentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        System.out.println("Authentication successful");
        System.out.println("Authenticated Principal: "
                + resultAuthentication.getName());

        String token = jwtUtil.generateJwtToken(resultAuthentication);

        System.out.println("JWT generated successfully");
        System.out.println("===== LOGIN COMPLETED =====");

        return ResponseEntity.ok(token);
    }


}
