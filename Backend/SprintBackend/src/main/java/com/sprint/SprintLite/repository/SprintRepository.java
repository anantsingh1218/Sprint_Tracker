package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    Optional<Sprint> findSprintBySprintName(String sprintName);

    Sprint findSprintBySprintCode(String sprintCode);
}