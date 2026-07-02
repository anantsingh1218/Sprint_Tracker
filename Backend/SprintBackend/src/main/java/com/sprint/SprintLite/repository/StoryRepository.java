package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.Status;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Long countByFeatureid(
            Feature feature
    );

    Long countByFeatureidAndStorystatus(
            Feature feature,
            Status status
    );

    Page<Story> findByUserid(
            Users user,
            Pageable pageable
    );
}