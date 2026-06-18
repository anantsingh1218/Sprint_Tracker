package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.UserProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductMappingRepository extends JpaRepository<UserProductMapping, Integer> {
}