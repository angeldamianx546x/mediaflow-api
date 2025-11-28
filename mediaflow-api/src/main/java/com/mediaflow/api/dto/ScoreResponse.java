package com.mediaflow.api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScoreResponse {
    @JsonProperty("score Id")
    Integer scoreId;
    
    Integer likes;
    
    Integer dislikes;
    
    BigDecimal calification;
    
    Integer views;
    
    BigDecimal impact;
}
