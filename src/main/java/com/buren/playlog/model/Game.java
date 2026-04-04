package com.buren.playlog.model;

import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="games")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

    @NotBlank
    private String name;

    @Column(length = 10000)
    private String description;

    @Column(name = "released", columnDefinition = "DATE")
    private LocalDate released;

    private String backgroundImage;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "game_platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "platform")
    private List<String> platforms;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "genre")
    private List<String> genre;

    @Column(unique = true)
    private Long rawgId;
}
