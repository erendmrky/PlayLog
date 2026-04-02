package com.buren.playlog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity{

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    @Column(name ="created_date", columnDefinition = "DATE")
    private LocalDate createdDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;
}
