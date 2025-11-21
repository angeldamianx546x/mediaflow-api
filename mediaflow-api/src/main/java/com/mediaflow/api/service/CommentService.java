package com.mediaflow.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mediaflow.api.dto.CommentRequest;
import com.mediaflow.api.dto.CommentRespose;

public interface CommentService {
    Page<CommentRespose> findByContentId(Integer contentId, Pageable pageable);
    
    CommentRespose findById(Integer commentId);
    
    CommentRespose create(CommentRequest req);
    
    CommentRespose update(Integer commentId, CommentRequest req);
    
    void delete(Integer commentId);
}
