package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    List<Feature> findBySprintId(Integer sprintId);

    List<Feature> findByProductId(Integer projectId);

    List<Feature> findByUpdatedAtBetween(
            Instant start,
            Instant end
    );

    Optional<Feature> findFeatureByFeatureCode(String featureCode);
    boolean findByFeatureCode(String featureCode);

    List<Feature> findFeaturesByProductId(Product productId);

    List<Feature> findFeaturesBySprintId(Sprint sprint);

    List<Feature> findByProductId(Product product);

    Long countByProductId(Product product);

    //List<FeatureResponseDto> getAllFeaturesBySto(Story story);

    List<Feature> findByTitleContainingIgnoreCase(String keyword);


}