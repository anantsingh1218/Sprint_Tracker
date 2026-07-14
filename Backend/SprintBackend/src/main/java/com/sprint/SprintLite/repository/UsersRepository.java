package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> readUsersByEmailOrderByUsername(@NotBlank(message = "Email is required") @Size(max = 255) String email, @NotBlank(message = "Username is required") @Size(max = 255) String username);

    List<Users> findByUsernameContainingIgnoreCase(String keyword);
}