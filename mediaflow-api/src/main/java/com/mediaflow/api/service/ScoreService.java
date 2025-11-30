package com.mediaflow.api.service;

import com.mediaflow.api.dto.ScoreRequest;
import com.mediaflow.api.dto.ScoreResponse;

public interface ScoreService {
    ScoreResponse findById(Integer scoreId);
    
    ScoreResponse findByContentId(Integer contentId);
    
    ScoreResponse create(Integer contentId, ScoreRequest req);
    
    ScoreResponse update(Integer scoreId, ScoreRequest req);
    
    void delete(Integer scoreId);
    
    // Métodos para actualizar métricas
    ScoreResponse incrementLikes(Integer contentId);
    
    ScoreResponse incrementDislikes(Integer contentId);
    
    ScoreResponse incrementViews(Integer contentId);
    
    ScoreResponse recalculateMetrics(Integer contentId);
}