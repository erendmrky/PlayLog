package com.buren.playlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record RawgGameResponse (
        List<GamePopularDTO> results
) {
    public record GamePopularDTO (
            Long id,
            String name,
            LocalDate released,
            @JsonProperty("background_image") String image,
            @JsonProperty("description") String description
    ) {}
}
