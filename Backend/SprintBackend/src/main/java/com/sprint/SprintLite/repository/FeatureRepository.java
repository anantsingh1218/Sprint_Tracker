package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
}