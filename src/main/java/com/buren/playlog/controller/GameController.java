package com.buren.playlog.controller;

import com.buren.playlog.dto.GameResponseDTO;
import com.buren.playlog.model.Game;
import com.buren.playlog.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.root}/games")
public class GameController extends AbstractController<Game, Long> {

    private final GameService gameService;

    public GameController(GameService gameService) {
        super(gameService);
        this.gameService = gameService;
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopular() {
        return ResponseEntity.ok(gameService.getPopular());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

}
