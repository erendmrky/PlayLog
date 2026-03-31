package com.buren.playlog.repository;

import com.buren.playlog.model.Game;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends AbstractRepository<Game, Long>{
}
