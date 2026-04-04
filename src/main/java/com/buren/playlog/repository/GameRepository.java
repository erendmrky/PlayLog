package com.buren.playlog.repository;

import com.buren.playlog.model.Game;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends AbstractRepository<Game, Long>{

    Optional<Game> findByRawgId(Long rawgId);
}
