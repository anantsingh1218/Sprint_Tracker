package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import com.sprint.SprintLite.entity.Task;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TaskRepository
        extends JpaRepository<Task, Integer> {

    Long countByTaskstatus(
            Status status
    );

    Long countByUserid(
            Users user
    );

    Long countByUseridAndTaskstatus(
            Users user,
            Status status
    );


    Long countBySprintid(
            Sprint sprint
    );

    Long countBySprintidAndTaskstatus(
            Sprint sprint,
            Status status
    );

    List<Task> findBySprintid_Id(Long sprintId);
    @Query("""
            SELECT COALESCE(
                SUM(t.originalestimatehours),
                0
            )
            FROM Task t
            WHERE t.sprintid = :sprint
            """)
    Integer getTotalEstimatedHours(
            @Param("sprint")
            Sprint sprint
    );

    @Query("""
SELECT COALESCE(
SUM(
t.remainingestimatehours
),
0
)
FROM Task t
WHERE t.sprintid=:sprint
""")
    Integer getRemainingHours(
            @Param("sprint")
            Sprint sprint
    );

    Long countByStoryid(
            Story story
    );

    Long countByStoryidAndTaskstatus(
            Story story,
            Status status
    );

    List<Task>
    findBySprintid(
            Sprint sprint
    );
}