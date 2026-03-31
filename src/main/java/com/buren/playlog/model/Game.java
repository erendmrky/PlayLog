package com.buren.playlog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="games")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

    @Column(name="title",unique = true)
    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @NotBlank
    private String platform;

    @Column(name = "release_date", columnDefinition = "DATE")
    private LocalDate releaseDate;
}
