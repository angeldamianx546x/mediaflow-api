package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.HistoryRequest;
import com.mediaflow.api.dto.HistoryResponse;
import com.mediaflow.api.model.History;

public final class HistoryMapper {

    public static HistoryResponse toResponse(History history) {
        if (history == null) {
            return null;
        }
        
        HistoryResponse.HistoryResponseBuilder builder = HistoryResponse.builder()
                .historyId(history.getHistoryId())
                .viewedAt(history.getViewedAt());
        
        if (history.getUser() != null) {
            builder.user(UserMapper.toResponse(history.getUser()));
        }
        
        if (history.getContent() != null) {
            builder.content(ContentMapper.toResponse(history.getContent()));
        }
        
        return builder.build();
    }

    public static History toEntity(HistoryRequest dto) {
        if (dto == null) {
            return null;
        }
        return History.builder()
                .viewedAt(dto.getViewedAt())
                .build();
    }

    public static void copyToEntity(HistoryRequest dto, History entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setViewedAt(dto.getViewedAt());
    }
}