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
import java.util.List;

@Entity
@Table(name="games")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

    @Column(name="name",unique = true)
    @NotBlank
    private String name;

    @Column(length = 10000)
    private String description;

    @Column(name = "released", columnDefinition = "DATE")
    private LocalDate released;

    private String backgroundImage;
    private List<String> platforms;
    private List<String> genre;

    private Long rawgId;
}
