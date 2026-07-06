package com.sprint.SprintLite.Feature.Service.impl;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.entity.Comment;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.CommentRepository;
import com.sprint.SprintLite.repository.FeatureRepository;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.Feature.Service.IFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements IFeatureService {

    private final FeatureRepository featureRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;

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
    public List<Feature> getFeaturesByProduct(Long productId) {
        return featureRepository.findByProductId(Math.toIntExact(productId));
    }

    @Override
    public List<Feature> getFeaturesBySprint(Long sprintId) {
        return featureRepository.findBySprintId(Math.toIntExact(sprintId));
    }

    @Override
    public Feature updateFeature(Long featureId, CreateFeatureRequest request) {

        Feature existingFeature = getFeatureById(featureId);

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (request.getTitle() != null) {
            existingFeature.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            existingFeature.setDescription(request.getDescription());
        }

        existingFeature.setUpdatedBy(username);
        existingFeature.setUpdatedAt(Instant.now());

        if (request.getComments() != null && !request.getComments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.getComments());
            comment.setCreatedBy(username);
            comment.setEntitytype(EntityType.FEATURE);
            comment.setEntityid(Math.toIntExact(featureId));
            comment.setCreatedAt(Instant.now());

            commentRepository.save(comment);
        }

        return featureRepository.save(existingFeature);
    }

    @Override
    public void deleteFeature(Long featureId) {

        Feature feature = getFeatureById(featureId);

        featureRepository.delete(feature);
    }

    public List<FeatureResponseDto> getAllFeatures() {
        List<Feature> features = featureRepository.findAll();

        return features.stream()
                .map(feature -> new FeatureResponseDto(
                        feature.getId(),
                        feature.getTitle(),
                        feature.getDescription(),
                        feature.getFeatureStatus()
                ))
                .toList();
    }
}
