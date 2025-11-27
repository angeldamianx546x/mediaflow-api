package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.EmotionRequest;
import com.mediaflow.api.dto.EmotionResponse;
import com.mediaflow.api.model.Emotion;

public final class EmotionMapper {

    public static EmotionResponse toResponse(Emotion emotion) {
        if (emotion == null) {
            return null;
        }
        return EmotionResponse.builder()
                .emotionId(emotion.getEmotionId())
                .name(emotion.getName())
                .build();
    }

    public static Emotion toEntity(Emotion dto){
        if(dto == null)
            return null;
        return Emotion.builder().name(dto.getName()).build();
    }

    public static void copyToEntity(EmotionRequest dto, Emotion entity){
        if(dto == null || entity == null)
            return;
        entity.setName(dto.getName());
    }
    
}
