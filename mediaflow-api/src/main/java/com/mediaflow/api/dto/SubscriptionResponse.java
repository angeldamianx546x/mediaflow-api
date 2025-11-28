package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscriptionResponse {
    @JsonProperty("follower")
    UserResponse follower;
    
    @JsonProperty("followed")
    UserResponse followed;
    
    @JsonProperty("subscribed at")
    LocalDateTime subscribedAt;
}