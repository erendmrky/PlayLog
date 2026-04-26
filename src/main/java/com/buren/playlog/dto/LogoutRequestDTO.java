package com.buren.playlog.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @NotBlank
        String token
) {
}
