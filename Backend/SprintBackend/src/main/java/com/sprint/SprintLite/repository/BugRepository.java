package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Bug;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {



    List<Bug> findBySprintid(Integer sprintid);

    List<Bug> findByStoryid(Integer storyId);

    Bug findBySprintidAndStoryid(
            Integer sprintId,
            Integer  storyId
    );

    List<Bug> findByUpdatedAtBetween(
            Instant start,
            Instant end
    );

    Long countByBugstatus(Status status);

    List<Bug> findByTitleContainingIgnoreCase(String keyword);
}