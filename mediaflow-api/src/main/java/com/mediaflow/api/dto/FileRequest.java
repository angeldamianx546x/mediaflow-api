package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import com.mediaflow.api.model.ContentType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FileRequest {
    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    @NotBlank
    @Size(max = 6)
    private String format;

    @NotNull
    private Integer fileSizeMB;

    @NotNull
    private Integer durationSeconds;

    @NotBlank
    @Size(max = 20)
    private String language;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private ContentType contentType; 

    private String description;

    @Min(0)
    private Integer recommendedAge;

    @NotBlank
    private String storageUrl;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private LocalDateTime created;

    // Relaciones (puedes enviar solo los IDs)
    @NotNull
    private Integer userId;

    @NotNull
    private Integer locationId;
}
