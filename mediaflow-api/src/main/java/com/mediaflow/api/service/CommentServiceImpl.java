package com.mediaflow.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mediaflow.api.dto.CommentRequest;
import com.mediaflow.api.dto.CommentRespose;
import com.mediaflow.api.mapper.CommentMapper;
import com.mediaflow.api.model.Comment;
import com.mediaflow.api.model.Content;
import com.mediaflow.api.repository.CommentRepository;
import com.mediaflow.api.repository.ContentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;

    @Override
    public Page<CommentRespose> findByContentId(Integer contentId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByContentId(contentId, pageable);
        return comments.map(CommentMapper::toResponse);
    }

    @Override
    public CommentRespose findById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));
        return CommentMapper.toResponse(comment);
    }

    @Override
    public CommentRespose create(CommentRequest req) {
        // Verificar que el contenido existe
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + req.getContentId()));

        // Convertir DTO a entidad
        Comment comment = CommentMapper.toEntity(req);
        comment.setContent(content);

        // Guardar comentario
        Comment saved = commentRepository.save(comment);

        return CommentMapper.toResponse(saved);
    }

    @Override
    public CommentRespose update(Integer commentId, CommentRequest req) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));

        CommentMapper.copyToEntity(req, comment);

        // Si se cambia el contentId
        if (req.getContentId() != null) {
            Content content = contentRepository.findById(req.getContentId())
                    .orElseThrow(() -> new EntityNotFoundException("Content not found: " + req.getContentId()));
            comment.setContent(content);
        }

        Comment updated = commentRepository.save(comment);
        return CommentMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment not found: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}