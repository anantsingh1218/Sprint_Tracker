package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Integer> {
}