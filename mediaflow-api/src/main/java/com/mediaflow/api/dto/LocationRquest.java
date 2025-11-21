package com.mediaflow.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocationRquest {
    @NotBlank
    @Size(max = 70)
    private String country;
    @NotBlank
    @Size(max = 70)
    private String region;
    @NotBlank
    @Size(max = 70)
    private String city;
    @NotBlank
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private float lat;
    @NotBlank
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private float lng;
}
