package com.mediaflow.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.HistoryRequest;
import com.mediaflow.api.dto.HistoryResponse;
import com.mediaflow.api.model.History;
import com.mediaflow.api.repository.HistoryRepository;
import com.mediaflow.api.service.AuthenticationService;
import com.mediaflow.api.service.HistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "History", description = "User viewing history management endpoints")
public class HistoryController {

    private final HistoryService historyService;
    private final AuthenticationService authenticationService;
    private final HistoryRepository historyRepository;

    @Operation(summary = "Get my history", description = "Returns viewing history of the authenticated user")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<Page<HistoryResponse>> getMyHistory(
            @PageableDefault(size = 20) Pageable pageable) {
        Integer currentUserId = authenticationService.getCurrentUserId();
        return ResponseEntity.ok(historyService.findByUserId(currentUserId, pageable));
    }

    @Operation(summary = "Get history by user", description = "Returns viewing history of a specific user. Only accessible by the user or admin.")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> getHistoryByUser(
            @PathVariable Integer userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        if (!authenticationService.canAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para ver este historial"));
        }
        
        return ResponseEntity.ok(historyService.findByUserId(userId, pageable));
    }

    @Operation(summary = "Get viewers by content", description = "Returns who has viewed a specific content. Only accessible by content owner or admin.")
    @GetMapping("/content/{contentId}")
    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    public ResponseEntity<Page<HistoryResponse>> getHistoryByContent(
            @PathVariable Integer contentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(historyService.findByContentId(contentId, pageable));
    }

    @Operation(summary = "Add content to history", description = "Adds a content to user's viewing history")
    @PostMapping("/add/{contentId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<HistoryResponse> addToHistory(@PathVariable Integer contentId) {
        Integer currentUserId = authenticationService.getCurrentUserId();
        HistoryResponse created = historyService.addToHistory(currentUserId, contentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Create history entry", description = "Creates a new history entry manually")
    @PostMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> createHistory(@Valid @RequestBody HistoryRequest request) {
        // Verificar que el usuario solo pueda crear historial para sí mismo
        Integer currentUserId = authenticationService.getCurrentUserId();
        if (!request.getUserId().equals(currentUserId) && !authenticationService.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No puedes crear historial para otro usuario"));
        }
        
        HistoryResponse created = historyService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Delete history entry", description = "Deletes a specific history entry. Only the owner or admin can delete.")
    @DeleteMapping("/{historyId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> deleteHistory(@PathVariable Integer historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException("History entry not found: " + historyId));
        
        if (!authenticationService.canAccess(history.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para eliminar esta entrada"));
        }
        
        historyService.delete(historyId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear my history", description = "Clears all viewing history of the authenticated user")
    @DeleteMapping("/me/clear")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<Void> clearMyHistory() {
        Integer currentUserId = authenticationService.getCurrentUserId();
        historyService.clearUserHistory(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear user history", description = "Clears all viewing history of a specific user. Only accessible by the user or admin.")
    @DeleteMapping("/user/{userId}/clear")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<?> clearUserHistory(@PathVariable Integer userId) {
        if (!authenticationService.canAccess(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(buildErrorResponse("No tienes permiso para limpiar este historial"));
        }
        
        historyService.clearUserHistory(userId);
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