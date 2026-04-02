package com.buren.playlog.dto;

import com.buren.playlog.model.Game;
import java.time.LocalDate;

public record ReviewResponseDTO(
        Long id,
        Integer rating,
        String comment,
        LocalDate createdDate,
        UserResponseDTO user,
        Game game
) {
}
