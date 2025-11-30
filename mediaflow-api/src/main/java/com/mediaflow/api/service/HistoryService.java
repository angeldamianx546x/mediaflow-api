package com.mediaflow.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mediaflow.api.dto.HistoryRequest;
import com.mediaflow.api.dto.HistoryResponse;

public interface HistoryService {
    Page<HistoryResponse> findByUserId(Integer userId, Pageable pageable);
    
    Page<HistoryResponse> findByContentId(Integer contentId, Pageable pageable);
    
    HistoryResponse findById(Integer historyId);
    
    HistoryResponse create(HistoryRequest req);
    
    HistoryResponse addToHistory(Integer userId, Integer contentId);
    
    void delete(Integer historyId);
    
    void clearUserHistory(Integer userId);
}
