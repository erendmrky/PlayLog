package com.buren.playlog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserRequestDTO(
        @Email
        @NotBlank
        String email,
        @Size(max = 50)
        @NotBlank
        String username,
        @NotBlank
        String password
) {}
