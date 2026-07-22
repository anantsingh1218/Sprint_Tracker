package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.UserProductMapping;
import com.sprint.SprintLite.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductMappingRepository
        extends JpaRepository<UserProductMapping,Integer>{

    List<UserProductMapping>
    findByUserid(
            Users user
    );

}