package com.buren.playlog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UserRequestDTO(
        @Email
        @NotBlank
        String email,
        @Size(max = 50)
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z_]\\w*$", message = "Username must start with a letter or underscore and contain only letters, digits, and underscores.")
        String username,
        @NotBlank
        String password
) {}
