package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ISprintService;
import com.sprint.SprintLite.dto.CreateSprintRequest;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements ISprintService {

    private final SprintRepository sprintRepository;
    private final ProductRepository productRepository;


    @Override
    public Sprint createSprint(CreateSprintRequest request) {
        Product product = productRepository.findById(Math.toIntExact(request.getProductId()))
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Sprint sprint = new Sprint();
        sprint.setSprintName(request.getSprintName());
        sprint.setSprintDuration(Math.toIntExact(request.getSprintDuration()));
//        sprint.setProduct(product);
//        sprint.setCreatedAt(LocalDateTime.now());

        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Sprint not found"));
    }

    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

//    @Override
//    public List<Sprint> getSprintsByProductId(Long productId) {
//        return sprintRepository.findByProductId(productId);
//    }

    @Override
    public Sprint updateSprint(Long id,CreateSprintRequest request) {
        Sprint sprint = getSprintById(id);

        sprint.setSprintName(request.getSprintName());
        sprint.setSprintDuration(Math.toIntExact(request.getSprintDuration()));
//        sprint.setUpdatedAt(LocalDateTime.now());
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint updateSprintStatus(Long id, SprintStatus status) {
        Sprint sprint = getSprintById(id);
        sprint.setStatus(status);
//        sprint.setUpdatedAt(LocalDateTime.now());
        return sprintRepository.save(sprint);

    }

    @Override
    public void deleteSprint(Long id) {
        Sprint sprint = getSprintById(id);
        sprintRepository.delete(sprint);
    }
}
