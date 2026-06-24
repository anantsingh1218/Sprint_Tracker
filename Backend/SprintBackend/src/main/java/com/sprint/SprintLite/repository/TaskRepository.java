package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findBySprintid_Id(Long sprintId);
}