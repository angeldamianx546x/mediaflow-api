package com.mediaflow.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmotionResponse {
    @JsonProperty("emotion Id")
    Integer emotionId;
    String name;
}
