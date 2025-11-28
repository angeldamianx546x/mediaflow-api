package com.mediaflow.api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HistoryResponse {
    @JsonProperty("history Id")
    Integer historyId;
    
    @JsonProperty("viewed at")
    LocalDate viewedAt;
    
    @JsonProperty("user")
    UserResponse user;
    
    @JsonProperty("content")
    ContentResponse content;
}