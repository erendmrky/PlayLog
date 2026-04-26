package com.buren.playlog.dto;

import java.time.LocalDate;

public record ErrorResponseDTO(
        ErrorCode error,
        LocalDate date
) {
    public record ErrorCode(
            ErrorCodeEnum code,
            String message
    ) {}

    public enum ErrorCodeEnum {
        PASSWORD_WRONG,
        ENTITY_EXISTS,
        RAWG,
        ENTITY_NOT_FOUND,
        TOKEN_INVALID,
        BAD_REQUEST,
        INTERNAL_SERVER_ERROR
    }
}
