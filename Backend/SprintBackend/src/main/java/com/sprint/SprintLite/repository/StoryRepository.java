package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Integer> {

    List<Story> findByUpdatedAtBetween(
            Instant start,
            Instant end
    );

    List<Story> findByTitleContainingIgnoreCase(String keyword);
}