package com.mediaflow.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreRequest {
    @NotNull
    @Min(0)
    private Integer likes;

    @NotNull
    @Min(0)
    private Integer dislikes;

    @NotNull
    @DecimalMin("0.0000")
    @DecimalMax("9.9999")
    private BigDecimal calification;

    @NotNull
    @Min(0)
    private Integer views;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal impact;
}
