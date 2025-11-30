package com.mediaflow.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.ScoreRequest;
import com.mediaflow.api.dto.ScoreResponse;
import com.mediaflow.api.model.Content;
import com.mediaflow.api.repository.ContentRepository;
import com.mediaflow.api.service.AuthenticationService;
import com.mediaflow.api.service.ScoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Scores", description = "Content scoring and metrics management endpoints")
public class ScoreController {

    private final ScoreService scoreService;
    private final AuthenticationService authenticationService;
    private final ContentRepository contentRepository;

    @Operation(summary = "Get score by content", description = "Returns the score metrics for a specific content")
    @GetMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<ScoreResponse> getScoreByContent(@PathVariable Integer contentId) {
        return ResponseEntity.ok(scoreService.findByContentId(contentId));
    }

    @Operation(summary = "Get score by ID", description = "Returns a specific score by ID")
    @GetMapping("/{scoreId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<ScoreResponse> getScoreById(@PathVariable Integer scoreId) {
        return ResponseEntity.ok(scoreService.findById(scoreId));
    }

    @Operation(summary = "Create score for content", description = "Creates a new score for content. Only accessible by content owner or admin.")
    @PostMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<?> createScore(
            @PathVariable Integer contentId,
            @Valid @RequestBody ScoreRequest request) {
        
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + contentId));
        
        if (!authenticationService.canAccess(content.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para crear score para este contenido"));
        }
        
        ScoreResponse created = scoreService.create(contentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update score", description = "Updates score metrics. Only accessible by content owner or admin.")
    @PutMapping("/{scoreId}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<?> updateScore(
            @PathVariable Integer scoreId,
            @Valid @RequestBody ScoreRequest request) {
        
        // Obtener el score y verificar permisos
        ScoreResponse score = scoreService.findById(scoreId);
        Content content = contentRepository.findById(score.getScoreId())
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
        
        if (!authenticationService.canAccess(content.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para actualizar este score"));
        }
        
        return ResponseEntity.ok(scoreService.update(scoreId, request));
    }

    @Operation(summary = "Delete score", description = "Deletes a score. Only accessible by content owner or admin.")
    @DeleteMapping("/{scoreId}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<?> deleteScore(@PathVariable Integer scoreId) {
        ScoreResponse score = scoreService.findById(scoreId);
        Content content = contentRepository.findById(score.getScoreId())
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
        
        if (!authenticationService.canAccess(content.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para eliminar este score"));
        }
        
        scoreService.delete(scoreId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Like content", description = "Increments likes for a content")
    @PostMapping("/content/{contentId}/like")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<ScoreResponse> likeContent(@PathVariable Integer contentId) {
        return ResponseEntity.ok(scoreService.incrementLikes(contentId));
    }

    @Operation(summary = "Dislike content", description = "Increments dislikes for a content")
    @PostMapping("/content/{contentId}/dislike")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<ScoreResponse> dislikeContent(@PathVariable Integer contentId) {
        return ResponseEntity.ok(scoreService.incrementDislikes(contentId));
    }

    @Operation(summary = "Increment views", description = "Increments view count for a content. This is usually called automatically when viewing content.")
    @PostMapping("/content/{contentId}/view")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<ScoreResponse> incrementViews(@PathVariable Integer contentId) {
        return ResponseEntity.ok(scoreService.incrementViews(contentId));
    }

    @Operation(summary = "Recalculate metrics", description = "Recalculates impact and rating metrics for a content. Only accessible by content owner or admin.")
    @PostMapping("/content/{contentId}/recalculate")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<?> recalculateMetrics(@PathVariable Integer contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content not found: " + contentId));
        
        if (!authenticationService.canAccess(content.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para recalcular las métricas de este contenido"));
        }
        
        return ResponseEntity.ok(scoreService.recalculateMetrics(contentId));
    }

    private java.util.Map<String, Object> buildErrorResponse(String message) {
        java.util.Map<String, Object> error = new java.util.HashMap<>();
        error.put("timestamp", java.time.Instant.now().toString());
        error.put("code", "FORBIDDEN");
        error.put("message", message);
        return error;
    }
}