package com.mediaflow.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mediaflow.api.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    @Query("SELECT c FROM Comment c WHERE c.content.contentId = :contentId ORDER BY c.createdAt DESC")
    Page<Comment> findByContentId(@Param("contentId") Integer contentId, Pageable pageable);
}
