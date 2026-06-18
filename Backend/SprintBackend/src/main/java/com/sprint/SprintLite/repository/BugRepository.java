package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Integer> {
}