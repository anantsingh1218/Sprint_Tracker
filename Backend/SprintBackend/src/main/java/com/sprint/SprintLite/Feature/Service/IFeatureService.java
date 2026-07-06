package com.sprint.SprintLite.Feature.Service;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;

import java.util.List;

public interface IFeatureService {

    RegisterResponseDto createFeature(CreateFeatureRequest request);

    FeatureResponseDto getFeatureByFeatureCode(String featureCode);

    List<FeatureResponseDto> getAllFeatures();

    List<FeatureResponseDto> getFeaturesByProduct(Product product);

    List<FeatureResponseDto> getFeaturesBySprint(Sprint sprint);

    FeatureResponseDto updateFeature(String featureCode, CreateFeatureRequest request);

    RegisterResponseDto deleteFeature(String featureCode);

}