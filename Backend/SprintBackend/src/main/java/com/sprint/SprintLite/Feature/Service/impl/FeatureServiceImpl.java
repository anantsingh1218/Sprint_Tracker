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
    public RegisterResponseDto createFeature(CreateFeatureRequest request) {
        Feature feature = this.createNewFeatureFromDto(request);
        Feature savedFeature = featureRepository.save(feature);
        return new RegisterResponseDto("Feature Created successfully with Code: " + savedFeature.getFeatureCode());
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
        existingFeature.setUpdatedBy(username);
        existingFeature.setUpdatedAt(Instant.now());

        if (request.comments() != null && !request.comments().isBlank()) {
            Comment comment = new Comment();
            comment.setComment(request.comments());
            comment.setEntitytype(EntityType.FEATURE);
            comment.setEntityid(Math.toIntExact(existingFeature.getId()));
            comment.setCreatedAt(Instant.now());
            comment.setCreatedBy(username);
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

    private Feature createNewFeatureFromDto(CreateFeatureRequest request) throws UsernameNotFoundException{
        Product product = productRepository.findProductByProductname(request.productCategory());
        Users user = usersRepository.findByUsername(request.assignedTo())
                .orElseThrow(() -> new UsernameNotFoundException("User Name not found"));
        Sprint sprint = sprintRepository.findSprintBySprintName(request.sprintName())
                .orElseThrow(() -> new UsernameNotFoundException("Sprint Name not found"));
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
        return new FeatureResponseDto(
                feature.getFeatureCode(),
                feature.getTitle(),
                feature.getDescription(),
                feature.getFeatureStatus(),
                feature.getPriority().toString(),
                feature.getSprintId().getSprintName(),
                feature.getRemainingStoryPoints(),
                feature.getEstimatedStoryPoints(),
                feature.getProductId().getProductname(),
                feature.getUserid().getUsername()
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
