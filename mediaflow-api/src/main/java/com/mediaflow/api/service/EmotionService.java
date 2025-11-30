package com.mediaflow.api.service;

import java.util.List;

import com.mediaflow.api.dto.EmotionRequest;
import com.mediaflow.api.dto.EmotionResponse;

public interface EmotionService {
    List<EmotionResponse> findAll();
    
    EmotionResponse findById(Integer emotionId);
    
    EmotionResponse create(EmotionRequest req);
    
    EmotionResponse update(Integer emotionId, EmotionRequest req);
    
    void delete(Integer emotionId);
}