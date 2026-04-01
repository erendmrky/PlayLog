package com.buren.playlog.service;

import com.buren.playlog.dto.GameRequestDTO;
import com.buren.playlog.model.Game;
import com.buren.playlog.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService extends AbstractService<Game, Long>{

    public GameService(GameRepository gameRepository) {
        super(gameRepository);
    }

    public void add(GameRequestDTO gameRequestDTO){
        Game game = new Game();
        game.setTitle(gameRequestDTO.title());
        game.setGenre(gameRequestDTO.genre());
        game.setPlatform(gameRequestDTO.platform());
        game.setReleaseDate(gameRequestDTO.releaseDate());

        abstractRepository.save(game);
    }

}
