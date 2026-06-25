package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.enums.SprintStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SprintRepository
        extends JpaRepository<Sprint,Integer>{

    Optional<Sprint>
    findFirstByStatus(
            SprintStatus status
    );

    Optional<Sprint>
    findByProductidAndStatus(
            Product product,
            SprintStatus status
    );

}