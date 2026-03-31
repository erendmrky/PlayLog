package com.buren.playlog.controller;

import com.buren.playlog.dto.GameRequestDTO;
import com.buren.playlog.dto.GameResponseDTO;
import com.buren.playlog.model.Game;
import com.buren.playlog.service.GameService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("${api.root}/games")
public class GameController extends AbstractController<Game, Long> {

    private final GameService gameService;

    public GameController(GameService gameService){
        super(gameService);
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody GameRequestDTO gameRequestDTO){
        GameResponseDTO responseDTO = gameService.add(gameRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> get(@PathVariable Long id){
        try{
            return ResponseEntity.ok(gameService.getGame(id));
        } catch (EntityNotFoundException _){
            return ResponseEntity.notFound().build();
        }
    }

}
