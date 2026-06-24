package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {
//    List<Sprint> findByProductId(Long productId);
}