package com.mediaflow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediaflow.api.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    
}
