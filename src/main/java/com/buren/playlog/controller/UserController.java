package com.buren.playlog.controller;

import com.buren.playlog.dto.UserRequestDTO;
import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.model.User;
import com.buren.playlog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("${api.root}/users")
public class UserController extends AbstractController<User, Long> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (EntityNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> add(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.add(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
