package com.buren.playlog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate released;

    @JsonProperty("background_image")
    private String backgroundImage;

    @JsonIgnore
    private List<String> platforms;
    @JsonIgnore
    private List<String> genres;

    @JsonProperty("platforms")
    private void unpackPlatforms(List<Map<String, Object>> platformsJson) {
        if (platformsJson == null) return;
        this.platforms = platformsJson.stream()
                .map(p -> (Map<String, Object>) p.get("platform"))
                .map(p -> (String) p.get("name"))
                .toList();
    }

    @JsonProperty("genres")
    private void unpackGenres(List<Map<String, Object>> genresJson) {
        if (genresJson == null) return;
        this.genres = genresJson.stream()
                .map(g -> (String) g.get("name"))
                .toList();
    }
}