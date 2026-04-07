package com.buren.playlog.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public record ErrorResponseDTO(
        String message,
        LocalDate date,
        HttpStatus status
) {
}
