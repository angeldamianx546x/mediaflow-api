package com.mediaflow.api.controller;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.EmotionRequest;
import com.mediaflow.api.dto.EmotionResponse;
import com.mediaflow.api.service.EmotionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/emotions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Emotions", description = "Emotion management endpoints")
public class EmotionController {

    private final EmotionService emotionService;

    @Operation(summary = "Get all emotions", description = "Returns all available emotions in the system")
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<List<EmotionResponse>> getAllEmotions() {
        return ResponseEntity.ok(emotionService.findAll());
    }

    @Operation(summary = "Get emotion by ID", description = "Returns a specific emotion")
    @GetMapping("/{emotionId}")
    @PreAuthorize("hasAnyRole('VIEWER', 'CREATOR', 'ADMIN')")
    public ResponseEntity<EmotionResponse> getEmotionById(@PathVariable Integer emotionId) {
        return ResponseEntity.ok(emotionService.findById(emotionId));
    }

    @Operation(summary = "Create new emotion", description = "Creates a new emotion. Only accessible by administrators.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmotionResponse> createEmotion(@Valid @RequestBody EmotionRequest request) {
        EmotionResponse created = emotionService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/emotions/" + created.getEmotionId()))
                .body(created);
    }

    @Operation(summary = "Update emotion", description = "Updates an emotion. Only accessible by administrators.")
    @PutMapping("/{emotionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmotionResponse> updateEmotion(
            @PathVariable Integer emotionId,
            @Valid @RequestBody EmotionRequest request) {
        return ResponseEntity.ok(emotionService.update(emotionId, request));
    }

    @Operation(summary = "Delete emotion", description = "Deletes an emotion. Only accessible by administrators.")
    @DeleteMapping("/{emotionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmotion(@PathVariable Integer emotionId) {
        emotionService.delete(emotionId);
    }
}
