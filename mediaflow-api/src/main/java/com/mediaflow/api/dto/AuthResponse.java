package com.mediaflow.api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    
    @JsonProperty("expires_in")
    private Long expiresIn;
    
    @JsonProperty("user_id")
    private Integer userId;
    
    @JsonProperty("user_name")
    private String userName;
    
    private String email;
    
    @JsonProperty("date_birth")
    private LocalDate dateBirth;
    
    private String[] roles;
    
    @JsonProperty("profile_id")
    private Integer profileId;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("preferred_language")
    private String preferredLanguage;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    private String bio;
}
