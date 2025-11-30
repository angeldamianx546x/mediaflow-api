package com.mediaflow.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediaflow.api.dto.ScoreRequest;
import com.mediaflow.api.dto.ScoreResponse;
import com.mediaflow.api.mapper.ScoreMapper;
import com.mediaflow.api.model.Content;
import com.mediaflow.api.model.Score;
import com.mediaflow.api.repository.ContentRepository;
import com.mediaflow.api.repository.ScoreRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final ContentRepository contentRepository;

    @Override
    public ScoreResponse findById(Integer scoreId) {
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new EntityNotFoundException("Score not found: " + scoreId));
        return ScoreMapper.toResponse(score);
    }

    @Override
    public ScoreResponse findByContentId(Integer contentId) {
        Score score = scoreRepository.findByContentId(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Score not found for content: " + contentId));
        return ScoreMapper.toResponse(score);
    }

    @Override
    @Transactional
    public ScoreResponse create(Integer contentId, ScoreRequest req) {
        // Verificar que el contenido existe
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + contentId));

        // Verificar que el contenido no tenga ya un score
        if (scoreRepository.findByContentId(contentId).isPresent()) {
            throw new IllegalStateException("Score already exists for content: " + contentId);
        }

        // Crear el score
        Score score = ScoreMapper.toEntity(req);
        score.setContent(content);
        
        // Calcular métricas
        calculateMetrics(score);
        
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ScoreResponse update(Integer scoreId, ScoreRequest req) {
        Score existing = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new EntityNotFoundException("Score not found: " + scoreId));
        
        ScoreMapper.copyToEntity(req, existing);
        
        // Recalcular métricas
        calculateMetrics(existing);
        
        Score saved = scoreRepository.save(existing);
        return ScoreMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer scoreId) {
        if (!scoreRepository.existsById(scoreId)) {
            throw new EntityNotFoundException("Score not found: " + scoreId);
        }
        scoreRepository.deleteById(scoreId);
    }

    @Override
    @Transactional
    public ScoreResponse incrementLikes(Integer contentId) {
        Score score = scoreRepository.findByContentId(contentId)
                .orElseGet(() -> createDefaultScore(contentId));
        
        score.setLikes(score.getLikes() + 1);
        calculateMetrics(score);
        
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ScoreResponse incrementDislikes(Integer contentId) {
        Score score = scoreRepository.findByContentId(contentId)
                .orElseGet(() -> createDefaultScore(contentId));
        
        score.setDislikes(score.getDislikes() + 1);
        calculateMetrics(score);
        
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ScoreResponse incrementViews(Integer contentId) {
        Score score = scoreRepository.findByContentId(contentId)
                .orElseGet(() -> createDefaultScore(contentId));
        
        score.setViews(score.getViews() + 1);
        calculateMetrics(score);
        
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ScoreResponse recalculateMetrics(Integer contentId) {
        Score score = scoreRepository.findByContentId(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Score not found for content: " + contentId));
        
        calculateMetrics(score);
        
        Score saved = scoreRepository.save(score);
        return ScoreMapper.toResponse(saved);
    }

    /**
     * Calcula las métricas de impacto y calificación
     * 
     * IMPACTO: (likes + dislikes) / views * 100
     * Ejemplo: 10 vistas, 2 likes, 1 dislike = (2+1)/10 * 100 = 30%
     * 
     * CALIFICACIÓN: likes / (likes + dislikes) * 10
     * Ejemplo: 10 likes, 4 dislikes = 10/(10+4) * 10 = 7.14/10
     */
    private void calculateMetrics(Score score) {
        int likes = score.getLikes();
        int dislikes = score.getDislikes();
        int views = score.getViews();
        
        // Calcular IMPACTO: (reacciones / vistas) * 100
        BigDecimal impact;
        if (views > 0) {
            int totalReactions = likes + dislikes;
            impact = BigDecimal.valueOf(totalReactions)
                    .divide(BigDecimal.valueOf(views), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            impact = BigDecimal.ZERO;
        }
        
        // Calcular CALIFICACIÓN: (likes / total_reacciones) * 10
        BigDecimal calification;
        int totalReactions = likes + dislikes;
        if (totalReactions > 0) {
            calification = BigDecimal.valueOf(likes)
                    .divide(BigDecimal.valueOf(totalReactions), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(10));
        } else {
            calification = BigDecimal.ZERO;
        }
        
        score.setImpact(impact);
        score.setCalification(calification);
    }

    /**
     * Crea un score por defecto para un contenido
     */
    private Score createDefaultScore(Integer contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + contentId));
        
        Score score = Score.builder()
                .likes(0)
                .dislikes(0)
                .views(0)
                .impact(BigDecimal.ZERO)
                .calification(BigDecimal.ZERO)
                .content(content)
                .build();
        
        return scoreRepository.save(score);
    }
}