package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository
        extends JpaRepository<Story,Integer>{

    Long countBySprintid(
            Sprint sprint
    );

    Long countBySprintidAndStorystatus(
            Sprint sprint,
            Status status
    );

}