package com.buren.playlog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(
        @Min(1)
        @Max(5)
        Integer rating,

        String comment,

        @NotNull
        Long userId,

        @NotNull
        Long gameId
) {
}
