package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.Status;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Integer> {

        Optional<Story> findByStoryCode(String storyCode);

        Long countBySprintid(Sprint sprint);

        Long countBySprintidAndStorystatus(Sprint sprint, Status status);

        Long countByFeatureid(Feature feature);

        Long countByFeatureidAndStorystatus(Feature feature, Status status);

        Page<Story> findByUserid(Users user, Pageable pageable);

        List<Story> findByUpdatedAtBetween(Instant start, Instant end);

        List<Story> findByFeatureid(Feature feature);

        List<Story> findByTitleContainingIgnoreCase(String keyword);
    }