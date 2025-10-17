package com.mediaflow.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    @JsonProperty("user Id")
    Integer userId;
    String name;
    String email;
    @JsonProperty("date birth")
    LocalDate dateBirth;
    List<String> roles;
    ProfileResponse profile;
    LocationResponse location;
}
