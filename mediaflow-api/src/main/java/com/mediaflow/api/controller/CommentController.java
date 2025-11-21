package com.mediaflow.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.mediaflow.api.dto.CommentRequest;
import com.mediaflow.api.dto.CommentRespose;
import com.mediaflow.api.model.Comment;
import com.mediaflow.api.repository.CommentRepository;
import com.mediaflow.api.service.AuthenticationService;
import com.mediaflow.api.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comments", description = "Comment management endpoints")
public class CommentController {

    private final CommentService commentService;
    private final AuthenticationService authenticationService;
    private final CommentRepository commentRepository;

    @Operation(summary = "Get comments by content", description = "Returns all comments for a specific content with pagination.")
    @GetMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<Page<CommentRespose>> getCommentsByContent(
            @PathVariable Integer contentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(commentService.findByContentId(contentId, pageable));
    }

    @Operation(summary = "Get comment by ID", description = "Returns a specific comment.")
    @GetMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<CommentRespose> getCommentById(@PathVariable Integer commentId) {
        return ResponseEntity.ok(commentService.findById(commentId));
    }

    @Operation(summary = "Create comment", description = "Creates a new comment on content.")
    @PostMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<CommentRespose> createComment(@Valid @RequestBody CommentRequest request) {
        CommentRespose created = commentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update comment", description = "Updates a comment. Only the owner or admin can update.")
    @PutMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> updateComment(
            @PathVariable Integer commentId,
            @Valid @RequestBody CommentRequest request) {
        
        // Verificar que el comentario existe
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));
        
        // Verificar que el usuario actual es el autor del comentario (por email/nombre)
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        if (!comment.getNameUser().equals(currentUserEmail) && !authenticationService.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para actualizar este comentario"));
        }
        
        return ResponseEntity.ok(commentService.update(commentId, request));
    }

    @Operation(summary = "Delete comment", description = "Deletes a comment. Only the owner or admin can delete.")
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        // Verificar que el comentario existe
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));
        
        // Verificar que el usuario actual es el autor del comentario o es admin
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        if (!comment.getNameUser().equals(currentUserEmail) && !authenticationService.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para eliminar este comentario"));
        }
        
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

    private java.util.Map<String, Object> buildErrorResponse(String message) {
        java.util.Map<String, Object> error = new java.util.HashMap<>();
        error.put("timestamp", java.time.Instant.now().toString());
        error.put("code", "FORBIDDEN");
        error.put("message", message);
        return error;
    }
}