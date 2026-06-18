package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}