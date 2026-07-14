package com.sprint.SprintLite.Feature.Controller;

import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.dto.RegisterResponseDto;
import com.sprint.SprintLite.Feature.Service.IFeatureService;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final IFeatureService featureService;
    private final ProductRepository productRepository;
    private final SprintRepository sprintRepository;

    @PostMapping("/add")
    public ResponseEntity<FeatureResponseDto> addFeature(@RequestBody CreateFeatureRequest request) {
        FeatureResponseDto responseDto = featureService.createFeature(request);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{featureCode}")
    public ResponseEntity<FeatureResponseDto> getFeatureById(@PathVariable String featureCode) {
        FeatureResponseDto featureResponseDto = featureService.getFeatureByFeatureCode(featureCode);
        return ResponseEntity.ok(featureResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<FeatureResponseDto>> getAllFeatures() {
        List<FeatureResponseDto> features = featureService.getAllFeatures();
        return ResponseEntity.ok(features);
    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<List<FeatureResponseDto>> getFeatureByProductId(@PathVariable String productCode) {
        Product product = productRepository.findProductByProductCode(productCode);
        List<FeatureResponseDto> featureResponseDtoList = featureService.getFeaturesByProduct(product);
        return ResponseEntity.ok(featureResponseDtoList);
    }

    @GetMapping("/sprint/{sprintCode}")
    public ResponseEntity<List<FeatureResponseDto>> getFeatureBySprintId(@PathVariable String sprintCode) {
        Sprint sprint = sprintRepository.findSprintBySprintCode(sprintCode);
        List<FeatureResponseDto> featureResponseDtoList = featureService.getFeaturesBySprint(sprint);
        return ResponseEntity.ok(featureResponseDtoList);
    }


    // Update Full Feature
    @PutMapping("/{featureCode}")
    public ResponseEntity<FeatureResponseDto> updateFeature(
            @PathVariable String featureCode,
            @RequestBody CreateFeatureRequest feature) {
        FeatureResponseDto updatedFeatureDto =
                featureService.updateFeature(featureCode, feature);
        return ResponseEntity.ok(updatedFeatureDto);
    }


    // Delete Feature
    @DeleteMapping("/{featureCode}")
    public ResponseEntity<RegisterResponseDto> deleteFeature(
            @PathVariable String featureCode) {
        RegisterResponseDto registerResponseDto = featureService.deleteFeature(featureCode);
        return ResponseEntity.ok(registerResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeatureResponseDto>> getAllFeaturesForDropdown() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }


}