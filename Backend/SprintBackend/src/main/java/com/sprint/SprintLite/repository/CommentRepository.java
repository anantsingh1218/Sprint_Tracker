package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}