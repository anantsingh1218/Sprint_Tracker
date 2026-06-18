package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
}