package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    List<Feature> findBySprintId(Sprint sprint);
    List<Feature> findByProductId(Integer projectId);

}