package com.mediaflow.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmotionRequest {
    @NotNull
    private String name;
}
