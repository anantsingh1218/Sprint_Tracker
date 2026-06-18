package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Worklog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorklogRepository extends JpaRepository<Worklog, Integer> {
}