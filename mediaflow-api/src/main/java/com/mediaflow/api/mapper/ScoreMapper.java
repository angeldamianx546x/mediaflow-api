package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.ScoreRequest;
import com.mediaflow.api.dto.ScoreResponse;
import com.mediaflow.api.model.Score;

public final class ScoreMapper {

    public static ScoreResponse toResponse(Score score) {
        if (score == null) {
            return null;
        }
        return ScoreResponse.builder()
                .scoreId(score.getScoreId())
                .likes(score.getLikes())
                .dislikes(score.getDislikes())
                .calification(score.getCalification())
                .views(score.getViews())
                .impact(score.getImpact())
                .build();
    }

    public static Score toEntity(ScoreRequest dto) {
        if (dto == null) {
            return null;
        }
        return Score.builder()
                .likes(dto.getLikes())
                .dislikes(dto.getDislikes())
                .calification(dto.getCalification())
                .views(dto.getViews())
                .impact(dto.getImpact())
                .build();
    }

    public static void copyToEntity(ScoreRequest dto, Score entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setLikes(dto.getLikes());
        entity.setDislikes(dto.getDislikes());
        entity.setCalification(dto.getCalification());
        entity.setViews(dto.getViews());
        entity.setImpact(dto.getImpact());
    }
}