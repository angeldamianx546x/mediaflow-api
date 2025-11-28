package com.mediaflow.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistoryRequest {
    @NotNull
    private LocalDate viewedAt;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer contentId;
}
