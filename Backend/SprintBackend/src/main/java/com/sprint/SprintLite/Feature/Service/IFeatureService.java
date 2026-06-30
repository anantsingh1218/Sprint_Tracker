package com.sprint.SprintLite.Feature.Service;

import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.enums.Status;

import java.util.List;

public interface IFeatureService {

    Feature createFeature(CreateFeatureRequest request);

    Feature getFeatureById(Long featureId);

    List<Feature> getAllFeatures();

    List<Feature> getFeaturesByProduct(Long productId);

    List<Feature> getFeaturesBySprint(Long sprintId);

    Feature updateFeature(Long featureId, CreateFeatureRequest feature);

    Feature updateFeatureStatus(Long featureId, Status status);

    void deleteFeature(Long featureId);
}