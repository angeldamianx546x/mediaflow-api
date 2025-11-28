package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @NotNull
    private Integer followerId;

    @NotNull
    private Integer followedId;

    private LocalDateTime subscribedAt;
}