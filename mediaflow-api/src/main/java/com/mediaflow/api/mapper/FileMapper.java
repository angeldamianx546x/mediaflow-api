package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.FileRequest;
import com.mediaflow.api.dto.FileResponse;
import com.mediaflow.api.model.File;

public final class FileMapper {

    public static FileResponse toResponse(File file) {
        if (file == null) {
            return null;
        }
        FileResponse.FileResponseBuilder builder = FileResponse.builder()
                .fileId(file.getFileId())
                .width(file.getWidth())
                .height(file.getHeight())
                .format(file.getFormat())
                .fileSizeMB(file.getFileSizeMB())
                .durationSeconds(file.getDurationSeconds())
                .language(file.getLanguage())
                .title(file.getTitle())
                .contentType(file.getContentType()) // ya es enum
                .description(file.getDescription())
                .recommendedAge(file.getRecommendedAge())
                .storageUrl(file.getStorageUrl())
                .thumbnailUrl(file.getThumbnailUrl())
                .created(file.getCreated());
        if (file.getLocation() != null) {
            builder.location(LocationMapper.toResponse(file.getLocation()));
        }
        if (file.getUser() != null) {
            builder.user(UserMapper.toResponse(file.getUser()));
        }
        return builder.build();
    }

    // 🔹 Convierte un DTO FileRequest a una entidad File
    public static File toEntity(FileRequest dto) {
        if (dto == null) {
            return null;
        }

        return File.builder()
                .width(dto.getWidth())
                .height(dto.getHeight())
                .format(dto.getFormat())
                .fileSizeMB(dto.getFileSizeMB())
                .durationSeconds(dto.getDurationSeconds())
                .language(dto.getLanguage())
                .title(dto.getTitle())
                .contentType(dto.getContentType())
                .description(dto.getDescription())
                .recommendedAge(dto.getRecommendedAge())
                .storageUrl(dto.getStorageUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .created(dto.getCreated())
                .build();
    }

    // 🔹 Copia los datos de un DTO FileRequest a una entidad File existente
    public static void copyToEntity(FileRequest dto, File entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setWidth(dto.getWidth());
        entity.setHeight(dto.getHeight());
        entity.setFormat(dto.getFormat());
        entity.setFileSizeMB(dto.getFileSizeMB());
        entity.setDurationSeconds(dto.getDurationSeconds());
        entity.setLanguage(dto.getLanguage());
        entity.setTitle(dto.getTitle());
        entity.setContentType(dto.getContentType()); // ya es enum
        entity.setDescription(dto.getDescription());
        entity.setRecommendedAge(dto.getRecommendedAge());
        entity.setStorageUrl(dto.getStorageUrl());
        entity.setThumbnailUrl(dto.getThumbnailUrl());
        entity.setCreated(dto.getCreated());

        // Relaciones: asignarlas en el servicio según IDs
        // entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        // entity.setLocation(locationRepository.findById(dto.getLocationId()).orElse(null));
    }
}
