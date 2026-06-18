package com.sprint.SprintLite.aspect;

import com.sprint.SprintLite.dto.RegisterUserDto;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.exception.RegistrationValidationException;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationValidationAspect {
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final UsersRepository usersRepository;

    @Before("""
        execution(* com.sprint.SprintLite.user.controller.UserController
        .registerUser(..))
        """)
    public void validateBeforeRegister(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RegisterUserDto request = (RegisterUserDto) args[0];
        log.info("Validating user registration request");
        Map<String, String> errors = new HashMap<>();

        // Compromised password check
        CompromisedPasswordDecision decision =
                compromisedPasswordChecker.check(request.password());
        if (decision.isCompromised()) {
            errors.put("password", "Choose a strong password");
        }
        // Existing user check
        Optional<Users> existingUser =
                usersRepository.readUsersByEmailOrderByUsername(
                        request.email(), request.username());

        if (existingUser.isPresent()) {
            Users user = existingUser.get();

            if (user.getEmail().equalsIgnoreCase(request.email())) {
                errors.put("email", "Email is already registered");
            }

            if (user.getUsername().equals(request.username())) {
                errors.put("userName", "User Name is already registered");
            }
        }

        // Stop execution if validation fails
        if (!errors.isEmpty()) {
            log.warn("Registration validation failed: {}", errors);
            throw new RegistrationValidationException(errors);
        }

        log.info("Registration validation passed");
    }
}
