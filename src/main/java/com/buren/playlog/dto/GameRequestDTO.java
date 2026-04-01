package com.buren.playlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record GameRequestDTO(
        @NotBlank
        String title,

        @NotBlank
        String genre,

        @NotBlank
        String platform,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate releaseDate
) {
}
