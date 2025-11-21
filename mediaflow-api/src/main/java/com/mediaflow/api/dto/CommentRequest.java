package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank
    private String avatarUrl;

    @NotBlank
    @Size(max = 70)
    private String nameUser;

    @NotBlank
    private String body;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Integer contentId;
}
