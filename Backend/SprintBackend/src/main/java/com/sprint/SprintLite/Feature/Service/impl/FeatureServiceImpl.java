package com.sprint.SprintLite.Feature.Service.impl;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.repository.*;
import com.sprint.SprintLite.Feature.Service.IFeatureService;
import com.sprint.SprintLite.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sprint.SprintLite.util.CodeUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeatureServiceImpl implements IFeatureService {

    private final FeatureRepository featureRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;
    private final UsersRepository usersRepository;
    private final SprintRepository sprintRepository;

    @Override
    @Transactional
    public FeatureResponseDto createFeature(CreateFeatureRequest request) {
        Feature feature = this.createNewFeatureFromDto(request);
        Feature savedFeature = featureRepository.save(feature);

        if (request.comments() != null && !request.comments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.comments());
            comment.setEntitytype(EntityType.FEATURE);
            comment.setEntityid(savedFeature.getId());
            comment.setCreatedBy(savedFeature.getCreatedBy());
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);
        }

        return this.mapFeatureToResponseDto(savedFeature);
    }

    @Override
    public FeatureResponseDto getFeatureByFeatureCode(String featureCode) throws RuntimeException {
        Feature feature = featureRepository.findFeatureByFeatureCode(featureCode)
                .orElseThrow(() -> new RuntimeException("Feature not found for Feature Code : " + featureCode));
        return this.mapFeatureToResponseDto(feature);
    }

    @Override
    public List<FeatureResponseDto> getAllFeatures() throws RuntimeException{
        List<FeatureResponseDto> featureResponseDtosList = new ArrayList<>();
        List<Feature> featureList = featureRepository.findAll();
        if (featureList.isEmpty()){
            throw new RuntimeException("No Features are added");
        }
        featureList.forEach(feature -> {
            FeatureResponseDto featureResponseDto = this.mapFeatureToResponseDto(feature);
            featureResponseDtosList.add(featureResponseDto);
        });
        return featureResponseDtosList;
    }

    @Override
    public List<FeatureResponseDto> getFeaturesByProduct(Product product) throws RuntimeException {
        List<Feature> featureList = featureRepository.findFeaturesByProductId(product);
        if (featureList.isEmpty()){
            throw new RuntimeException("Features not found for Product : " + product.getProductname());
        }
        return this.mapFeatureListToDtos(featureList);
    }

    @Override
    public List<FeatureResponseDto> getFeaturesBySprint(Sprint sprint) throws RuntimeException {
        List<Feature> featureList = featureRepository.findFeaturesBySprintId(sprint);
        if (featureList.isEmpty()){
            throw new RuntimeException("Features not found for Sprint : " + sprint.getSprintName());
        }
        return this.mapFeatureListToDtos(featureList);
    }

    @Override
    @Transactional
    public FeatureResponseDto updateFeature(String featureCode, CreateFeatureRequest request) throws RuntimeException {

        Feature existingFeature = featureRepository.findFeatureByFeatureCode(featureCode)
                .orElseThrow(() -> new RuntimeException("Feature not found for Feature Code : " + featureCode));

        String username = ApplicationUtility.getLoggedInUser();

        if (request.featureTitle() != null) {
            existingFeature.setTitle(request.featureTitle());
        }
        if (request.description() != null) {
            existingFeature.setDescription(request.description());
        }

        if (request.productCategory() != null && !request.productCategory().isBlank()) {
            existingFeature.setProductId(productRepository.findProductByProductname(request.productCategory()));
        } else if (request.productCategory() != null && request.productCategory().isBlank()) {
            existingFeature.setProductId(null);
        }

        if (request.sprintName() != null && !request.sprintName().isBlank()) {
            sprintRepository.findSprintBySprintName(request.sprintName()).ifPresent(existingFeature::setSprintId);
        } else if (request.sprintName() != null && request.sprintName().isBlank()) {
            existingFeature.setSprintId(null);
        }

        if (request.assignedTo() != null && !request.assignedTo().isBlank() && !request.assignedTo().equalsIgnoreCase("SYSTEM")) {
            usersRepository.findByUsername(request.assignedTo()).ifPresent(existingFeature::setUserid);
        } else if (request.assignedTo() != null && (request.assignedTo().isBlank() || request.assignedTo().equalsIgnoreCase("SYSTEM"))) {
            usersRepository.findByUsername("System").ifPresent(existingFeature::setUserid);
        }

        if (request.featureStatus() != null) {
            existingFeature.setFeatureStatus(request.featureStatus());
        }
        if (request.featurePriority() != null) {
            existingFeature.setPriority(request.featurePriority());
        }
        if (request.estimatedStoryPoints() != null) {
            existingFeature.setEstimatedStoryPoints(request.estimatedStoryPoints());
        }
        if (request.remainingStoryPoints() != null) {
            existingFeature.setRemainingStoryPoints(request.remainingStoryPoints());
        }

        existingFeature.setUpdatedBy(username);
        existingFeature.setUpdatedAt(Instant.now());

        if (request.comments() != null && !request.comments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.comments());
            comment.setEntitytype(EntityType.FEATURE);
            comment.setEntityid(existingFeature.getId());
            comment.setCreatedBy(username);
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);
        }

        Feature savedFeature = featureRepository.save(existingFeature);
        return this.mapFeatureToResponseDto(savedFeature);
    }

    @Override
    @Transactional
    public RegisterResponseDto deleteFeature(String featureCode) throws RuntimeException {

        Feature feature = featureRepository.findFeatureByFeatureCode(featureCode)
                .orElseThrow(() -> new RuntimeException("Feature not found for Feature Code : " + featureCode));

        featureRepository.delete(feature);
        return new RegisterResponseDto("Deleted Feature with Feature code : " + featureCode);
    }

    private Feature createNewFeatureFromDto(CreateFeatureRequest request) {
        Product product = null;
        if (request.productCategory() != null && !request.productCategory().isBlank()) {
            product = productRepository.findProductByProductname(request.productCategory());
        }

        Users user = null;
        if (request.assignedTo() != null && !request.assignedTo().isBlank() && !request.assignedTo().equalsIgnoreCase("SYSTEM")) {
            user = usersRepository.findByUsername(request.assignedTo())
                    .orElseThrow(() -> new UsernameNotFoundException("User Name not found: " + request.assignedTo()));
        } else {
            // Assign to current user if unassigned, since Feature requires a User
            String currentUsername = ApplicationUtility.getLoggedInUser();
            if (currentUsername == null || currentUsername.equalsIgnoreCase("anonymousUser")) currentUsername = "System";
            user = usersRepository.findByUsername(currentUsername).orElse(null);
        }

        Sprint sprint = null;
        if (request.sprintName() != null && !request.sprintName().isBlank()) {
            sprint = sprintRepository.findSprintBySprintName(request.sprintName())
                    .orElseThrow(() -> new RuntimeException("Sprint Name not found: " + request.sprintName()));
        }

        Feature newFeature = new Feature();
        newFeature.setTitle(request.featureTitle());
        newFeature.setDescription(request.description());
        newFeature.setProductId(product);
        newFeature.setSprintId(sprint);
        newFeature.setUserid(user);
        newFeature.setFeatureStatus(request.featureStatus());
        newFeature.setPriority(request.featurePriority());
        newFeature.setEstimatedStoryPoints(request.estimatedStoryPoints());
        newFeature.setRemainingStoryPoints(request.remainingStoryPoints());
        newFeature.setCreatedBy(ApplicationUtility.getLoggedInUser());
        newFeature.setCreatedAt(Instant.now());
        return newFeature;
    }

    private FeatureResponseDto mapFeatureToResponseDto(Feature feature){
        java.util.List<com.sprint.SprintLite.dto.CommentDto> commentDtos = commentRepository
                .findByEntitytypeAndEntityid(EntityType.FEATURE, feature.getId())
                .stream()
                .map(c -> {
                    com.sprint.SprintLite.dto.CommentDto cDto = new com.sprint.SprintLite.dto.CommentDto();
                    cDto.setUserCode(c.getCreatedBy());
                    cDto.setText(c.getComment());
                    cDto.setCreatedAt(c.getCreatedAt());
                    return cDto;
                }).toList();

        return new FeatureResponseDto(
                feature.getFeatureCode(),
                feature.getTitle(),
                feature.getDescription(),
                feature.getFeatureStatus(),
                feature.getPriority().toString(),
                feature.getSprintId().getSprintName(),
                feature.getRemainingStoryPoints(),
                feature.getEstimatedStoryPoints(),
                feature.getProductId() != null ? feature.getProductId().getProductname() : null,
                feature.getUserid().getUsername(),
                commentDtos
        );
    }

    private List<FeatureResponseDto> mapFeatureListToDtos(List<Feature> featureList){
        List<FeatureResponseDto> featureResponseDtoList = new ArrayList<>();
        featureList.forEach(feature -> {
            FeatureResponseDto featureResponseDto = this.mapFeatureToResponseDto(feature);
            featureResponseDtoList.add(featureResponseDto);
        });
        return featureResponseDtoList;
    }
}