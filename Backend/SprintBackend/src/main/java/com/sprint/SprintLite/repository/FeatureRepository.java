package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    List<Feature> findBySprintId(Integer sprintId);

    List<Feature> findByProductId(Integer projectId);

    List<Feature> findByUpdatedAtBetween(
            Instant start,
            Instant end
    );

    //List<FeatureResponseDto> getAllFeaturesBySto(Story story);

    List<Feature> findByTitleContainingIgnoreCase(String keyword);


}