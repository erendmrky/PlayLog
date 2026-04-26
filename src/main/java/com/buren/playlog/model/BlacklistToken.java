package com.buren.playlog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blacklisted_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BlacklistToken extends BaseEntity{

    @Column(name = "Blacklisted")
    private String token;
}
