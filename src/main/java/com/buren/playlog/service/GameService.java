package com.buren.playlog.service;

import com.buren.playlog.dto.GameResponseDTO;
import com.buren.playlog.dto.RawgGameResponse;
import com.buren.playlog.exceptions.RawgException;
import com.buren.playlog.model.Game;
import com.buren.playlog.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GameService extends AbstractService<Game, Long>{

    @Value("${rawg.apikey}")
    private String rawgApiKey;

    private final RestClient rawgClient;
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository, RestClient rawgClient) {
        super(gameRepository);
        this.gameRepository = gameRepository;
        this.rawgClient = rawgClient;
    }

    @Cacheable("popularGames")
    public List<RawgGameResponse.GamePopularDTO> getPopular() {
        RawgGameResponse response =  rawgClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("games")
                        .queryParam("key", rawgApiKey)
                        .queryParam("page", 1)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new RawgException("RAWG API Error " + res.getStatusCode());
                })
                .body(RawgGameResponse.class);

        return response != null ? response.results() : List.of();
    }

    public GameResponseDTO getGame(Long id) {
        try {
            return dtoFrom(gameRepository.findByRawgId(id)
                    .filter(Game::isActive)
                    .orElseThrow(() -> new EntityNotFoundException("Game not found")));
        } catch (EntityNotFoundException _) {
            GameResponseDTO gameResponseDTO =  rawgClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("games/{id}")
                            .queryParam("key", rawgApiKey)
                            .build(id))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                        throw new RawgException("No game found or RAW API Error " + res.getStatusCode());
                    }))
                    .body(GameResponseDTO.class);

            if (gameResponseDTO != null) {
                abstractRepository.save(fromDTO(gameResponseDTO));
                return gameResponseDTO;
            }
            return null;
        }
    }

    public static GameResponseDTO dtoFrom(Game game) {
        return new GameResponseDTO(game.getRawgId(), game.getName(), game.getDescription(), game.getReleased(), game.getBackgroundImage(), game.getPlatforms(), game.getGenre());
    }

    public static Game fromDTO(GameResponseDTO dto) {
        return new Game(dto.getName(), dto.getDescription(), dto.getReleased(), dto.getBackgroundImage(), dto.getPlatforms(), dto.getGenres(), dto.getId());
    }
}
