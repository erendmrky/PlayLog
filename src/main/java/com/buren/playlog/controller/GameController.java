package com.buren.playlog.controller;

import com.buren.playlog.dto.GameResponseDTO;
import com.buren.playlog.model.Game;
import com.buren.playlog.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.root}/games")
@Tag(name = "Game")
public class GameController extends AbstractController<Game, Long> {

    private final GameService gameService;

    public GameController(GameService gameService) {
        super(gameService);
        this.gameService = gameService;
    }

    @Operation(summary = "Get popular games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Games returns successfully"),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "Games not found.")
    })
    @GetMapping("/popular")
    public ResponseEntity<?> getPopular() {
        return ResponseEntity.ok(gameService.getPopular());
    }

    @Operation(summary = "Gets the specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Game returns successfully"),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "Game not found.")

    })
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

}
