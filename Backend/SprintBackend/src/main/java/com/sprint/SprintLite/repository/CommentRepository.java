package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Comment;
import com.sprint.SprintLite.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
List<Comment> findByEntitytypeAndEntityid(EntityType entitytype,Integer entityid);
}