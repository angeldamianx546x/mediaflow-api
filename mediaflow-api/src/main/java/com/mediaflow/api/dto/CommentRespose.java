package com.mediaflow.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentRespose {
    @JsonProperty("comment Id")
    Integer commentId;
    
    @JsonProperty("avatar url")
    String avatarUrl;
    
    @JsonProperty("name user")
    String nameUser;
    
    String body;
    
    @JsonProperty("created at")
    LocalDateTime createdAt;
    
    @JsonProperty("content id")
    Integer contentId;
}
