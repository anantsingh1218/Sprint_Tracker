package com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.impl;

import com.sprint.SprintLite.Sprint_and_Story_and_Task.Service.ISprintService;
import com.sprint.SprintLite.backlog.dto.FeatureResponseDto;
import com.sprint.SprintLite.dto.CreateSprintRequest;
import com.sprint.SprintLite.dto.SprintResponseDto;
import com.sprint.SprintLite.entity.Feature;
import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;
import com.sprint.SprintLite.repository.ProductRepository;
import com.sprint.SprintLite.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.SprintLite.util.CodeUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements ISprintService {

    private final SprintRepository sprintRepository;
    private final ProductRepository productRepository;


    @Override
    public SprintResponseDto createSprint(CreateSprintRequest request) {
        Product product = productRepository.findById(CodeUtils.decodeToInteger("P", request.getProductCode()))
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Sprint sprint = new Sprint();
        sprint.setSprintName(request.getSprintName());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setSprintDuration(Math.toIntExact(request.getSprintDuration()));
        sprint.setStatus(request.getStatus());
        sprint.setProductid(product);

        Sprint savedSprint = sprintRepository.save(sprint);

        SprintResponseDto response = new SprintResponseDto();
        response.setId(savedSprint.getId());
        response.setSprintName(savedSprint.getSprintName());
        response.setStartDate(savedSprint.getStartDate());
        response.setEndDate(savedSprint.getEndDate());
        response.setSprintDuration(savedSprint.getSprintDuration());
        response.setStatus(savedSprint.getStatus());
        response.setId(savedSprint.getId());

        return response;
    }

    @Override
    public SprintResponseDto getSprintById(Long id) {
        Sprint sprint = sprintRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        return mapToDto(sprint);
    }

    @Override
    public List<SprintResponseDto> getAllSprints() {
//        return sprintRepository.findAll()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
        List<SprintResponseDto> sprintResponseDtoList = new ArrayList<>();
        List<Sprint> sprintList = sprintRepository.findAll();
        sprintList.forEach(sprint -> {
            SprintResponseDto sprintResponseDto = mapToDto(sprint);
            sprintResponseDtoList.add(sprintResponseDto);
        });
        return sprintResponseDtoList;
    }
//    @Override
//    public List<Sprint> getSprintsByProductId(Long productId) {
//        return sprintRepository.findByProductId(productId);
//    }

    @Override
    public Sprint updateSprint(Long id, CreateSprintRequest request) {

        Sprint sprint = sprintRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        if (request.getSprintName() != null && !request.getSprintName().isEmpty()) {
            sprint.setSprintName(request.getSprintName());
        }


        if (request.getStartDate() != null) {
            sprint.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            sprint.setEndDate(request.getEndDate());
        }

        if (request.getSprintDuration() != null) {
            sprint.setSprintDuration(Math.toIntExact(request.getSprintDuration()));
        }

        if (request.getStatus() != null) {
            sprint.setStatus(request.getStatus());
        }

        if (request.getProductCode() != null) {
            Product product = productRepository.findById(CodeUtils.decodeToInteger("P", request.getProductCode()))
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            sprint.setProductid(product);
        }

        return sprintRepository.save(sprint);
    }

    public SprintResponseDto mapToDto(Sprint sprint) {
        return new SprintResponseDto(
                sprint.getId(),
                sprint.getSprintName(),
                CodeUtils.encode("P", sprint.getProductid() != null ? sprint.getProductid().getId() : null),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getSprintDuration(),
                sprint.getStatus()
        );
    }

    @Override
    public SprintResponseDto updateSprintStatus(Long id, SprintStatus status) {
        Sprint sprint = sprintRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        sprint.setStatus(status);

        Sprint updatedSprint = sprintRepository.save(sprint);

        return mapToDto(updatedSprint);
    }

    @Override
    public void deleteSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id.intValue())
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        sprintRepository.delete(sprint);
    }
}