package com.mediaflow.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationResponse {
    @JsonProperty("location id")
    Integer locationId;
    String country;
    String region;
    String city;
    float lat;
    float lng;
}
