package com.buren.playlog.dto;

import java.time.LocalDate;

public record GameResponseDTO(
        Long id,

        String title,

        String genre,

        String platform,

        LocalDate releaseDate
) {
}
