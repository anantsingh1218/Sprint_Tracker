package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.Worklog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorklogRepository extends JpaRepository<Worklog, Integer> {
    List<Worklog> findByTaskid(
            Task task
    );
}