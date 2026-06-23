package com.sprint.SprintLite.Feature.Controller;

import ch.qos.logback.core.status.Status;
import com.sprint.SprintLite.dto.CreateFeatureRequest;
import com.sprint.SprintLite.dto.CreateProductRequest;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.Feature.Service.IFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final IFeatureService featureService;

    @PostMapping("/add")
    public ResponseEntity<Feature> addFeature(@RequestBody CreateFeatureRequest request) {
        Feature feature = featureService.createFeature(request);
        return ResponseEntity.ok().body(feature);
    }

    @GetMapping("/{featureId}")
    public ResponseEntity<Feature> getFeatureById(@PathVariable Long featureId) {
        Feature feature = featureService.getFeatureById(featureId);
        return ResponseEntity.ok().body(feature);
    }

    @GetMapping
    public ResponseEntity<List<Feature>> getAllFeatures() {
        List<Feature> features = featureService.getAllFeatures();
        return ResponseEntity.ok().body(features);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Feature>> getFeatureByProductId(@PathVariable Long productId) {
        List<Feature> features = featureService.getFeaturesByProduct(productId);
        return ResponseEntity.ok().body(features);
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<Feature>> getFeatureBySprintId(@PathVariable Long sprintId) {
        List<Feature> features = featureService.getFeaturesBySprint(sprintId);
        return ResponseEntity.ok().body(features);
    }


    // Update Full Feature
    @PutMapping("/{featureId}")
    public ResponseEntity<Feature> updateFeature(
            @PathVariable Long featureId,
            @RequestBody Feature feature) {

        Feature updatedFeature =
                featureService.updateFeature(featureId, feature);

        return ResponseEntity.ok(updatedFeature);
    }


    // Delete Feature
    @DeleteMapping("/{featureId}")
    public ResponseEntity<String> deleteFeature(
            @PathVariable Long featureId) {

        featureService.deleteFeature(featureId);

        return ResponseEntity.ok("Feature deleted successfully");
    }



}



