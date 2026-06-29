package com.sprint.SprintLite.Feature.Service.impl;

import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.FeatureRepository;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.Feature.Service.IFeatureService;
import com.sprint.SprintLite.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements IFeatureService {

    private final FeatureRepository featureRepository;
    private final ProductRepository productRepository;

    @Override
    public Feature createFeature(CreateFeatureRequest request) {

        Product product = productRepository.findById(Math.toIntExact(request.getProductId()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Feature feature = new Feature();

        feature.setTitle(request.getTitle());
        feature.setDescription(request.getDescription());
        feature.setProductId(product);   // THIS LINE FIXES IT
        feature.setCreatedAt(Instant.now());

        return featureRepository.save(feature);
    }

    @Override
    public Feature getFeatureById(Long featureId) {
        return featureRepository.findById(Math.toIntExact(featureId))
                .orElseThrow(() ->
                        new RuntimeException("Feature not found"));
    }

    @Override
    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    @Override
    public List<Feature> getFeaturesByProduct(Long productId) {
        return featureRepository.findByProductId(Math.toIntExact(productId));
    }

    @Autowired
    private SprintRepository sprintRepository;

    @Override
    public List<Feature> getFeaturesBySprint(
            Long sprintId
    ){

        Sprint sprint =
                sprintRepository
                        .findById(
                                Math.toIntExact(
                                        sprintId
                                )
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Sprint not found"
                                        )
                        );

        return featureRepository
                .findBySprintId(
                        sprint
                );
    }

    @Override
    public Feature updateFeature(Long featureId, Feature updatedFeature) {

        Feature existingFeature = getFeatureById(featureId);

        existingFeature.setTitle(updatedFeature.getTitle());
        existingFeature.setDescription(updatedFeature.getDescription());
        existingFeature.setFeatureStatus(updatedFeature.getFeatureStatus());
        existingFeature.setPriority(updatedFeature.getPriority());
        existingFeature.setEstimatedStoryPoints(
                updatedFeature.getEstimatedStoryPoints()
        );
        existingFeature.setRemainingStoryPoints(
                updatedFeature.getRemainingStoryPoints()
        );
        existingFeature.setUpdatedAt(Instant.now());

        return featureRepository.save(existingFeature);
    }

    @Override
    public Feature updateFeatureStatus(Long featureId, Status status) {

        Feature feature = getFeatureById(featureId);

        feature.setFeatureStatus(status);
        feature.setUpdatedAt(Instant.now());

        return featureRepository.save(feature);
    }

    @Override
    public void deleteFeature(Long featureId) {

        Feature feature = getFeatureById(featureId);

        featureRepository.delete(feature);
    }
}