package com.mediaflow.api.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediaflow.api.dto.HistoryRequest;
import com.mediaflow.api.dto.HistoryResponse;
import com.mediaflow.api.mapper.HistoryMapper;
import com.mediaflow.api.model.Content;
import com.mediaflow.api.model.History;
import com.mediaflow.api.model.User;
import com.mediaflow.api.repository.ContentRepository;
import com.mediaflow.api.repository.HistoryRepository;
import com.mediaflow.api.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ScoreService scoreService;

    @Override
    public Page<HistoryResponse> findByUserId(Integer userId, Pageable pageable) {
        Page<History> histories = historyRepository.findByUserId(userId, pageable);
        return histories.map(HistoryMapper::toResponse);
    }

    @Override
    public Page<HistoryResponse> findByContentId(Integer contentId, Pageable pageable) {
        Page<History> histories = historyRepository.findByContentId(contentId, pageable);
        return histories.map(HistoryMapper::toResponse);
    }

    @Override
    public HistoryResponse findById(Integer historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException("History not found: " + historyId));
        return HistoryMapper.toResponse(history);
    }

    @Override
    @Transactional
    public HistoryResponse create(HistoryRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + req.getUserId()));
        
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + req.getContentId()));
        
        History history = HistoryMapper.toEntity(req);
        history.setUser(user);
        history.setContent(content);
        
        History saved = historyRepository.save(history);
        
        // Incrementar vistas del contenido
        scoreService.incrementViews(content.getContentId());
        
        return HistoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public HistoryResponse addToHistory(Integer userId, Integer contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + contentId));
        
        History history = History.builder()
                .viewedAt(LocalDate.now())
                .user(user)
                .content(content)
                .build();
        
        History saved = historyRepository.save(history);
        
        // Incrementar vistas del contenido
        scoreService.incrementViews(contentId);
        
        return HistoryMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer historyId) {
        if (!historyRepository.existsById(historyId)) {
            throw new EntityNotFoundException("History not found: " + historyId);
        }
        historyRepository.deleteById(historyId);
    }

    @Override
    @Transactional
    public void clearUserHistory(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        
        Page<History> histories = historyRepository.findByUserId(userId, Pageable.unpaged());
        historyRepository.deleteAll(histories.getContent());
    }
}