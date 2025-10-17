package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mediaflow.api.model.ContentType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileResponse {
    @JsonProperty("file id")
    Integer fileId;

    Integer width;
    Integer height;
    String format;

    @JsonProperty("file size (MB)")
    Integer fileSizeMB;

    @JsonProperty("duration (seconds)")
    Integer durationSeconds;

    String language;
    String title;

    @JsonProperty("content type")
    ContentType contentType;

    String description;

    @JsonProperty("recommended age")
    Integer recommendedAge;

    @JsonProperty("storage URL")
    String storageUrl;

    @JsonProperty("thumbnail URL")
    String thumbnailUrl;

    LocalDateTime created;

    // Relaciones
    @JsonProperty("location")
    LocationResponse location;

    @JsonProperty("user")
    UserResponse user;
}
