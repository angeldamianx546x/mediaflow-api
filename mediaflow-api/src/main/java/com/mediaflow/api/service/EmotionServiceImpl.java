package com.mediaflow.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mediaflow.api.dto.EmotionRequest;
import com.mediaflow.api.dto.EmotionResponse;
import com.mediaflow.api.mapper.EmotionMapper;
import com.mediaflow.api.model.Emotion;
import com.mediaflow.api.repository.EmotionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

    private final EmotionRepository repository;

    @Override
    public List<EmotionResponse> findAll() {
        return repository.findAll().stream()
                .map(EmotionMapper::toResponse)
                .toList();
    }

    @Override
    public EmotionResponse findById(Integer emotionId) {
        Emotion emotion = repository.findById(emotionId)
                .orElseThrow(() -> new EntityNotFoundException("Emotion not found: " + emotionId));
        return EmotionMapper.toResponse(emotion);
    }

    @Override
    public EmotionResponse create(EmotionRequest req) {
        Emotion emotion = EmotionMapper.toEntity(req);
        Emotion saved = repository.save(emotion);
        return EmotionMapper.toResponse(saved);
    }

    @Override
    public EmotionResponse update(Integer emotionId, EmotionRequest req) {
        Emotion existing = repository.findById(emotionId)
                .orElseThrow(() -> new EntityNotFoundException("Emotion not found: " + emotionId));
        EmotionMapper.copyToEntity(req, existing);
        Emotion saved = repository.save(existing);
        return EmotionMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer emotionId) {
        if (!repository.existsById(emotionId)) {
            throw new EntityNotFoundException("Emotion not found: " + emotionId);
        }
        repository.deleteById(emotionId);
    }
}