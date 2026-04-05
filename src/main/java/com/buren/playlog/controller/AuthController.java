package com.buren.playlog.controller;

import com.buren.playlog.dto.LoginRequestDTO;
import com.buren.playlog.dto.TokenResponseDTO;
import com.buren.playlog.dto.UserRequestDTO;
import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.service.AuthService;
import com.buren.playlog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("${api.root}/auth")
@Tag(name="Auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    @Operation(summary = "New user registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "User added successfully"),
            @ApiResponse(responseCode = "400",description = "Invalid input."),
            @ApiResponse(responseCode = "409",description = "User is already in the system.")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequestDTO requestDTO){
        UserResponseDTO responseDTO = userService.add(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "User log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successfully logged in."),
            @ApiResponse(responseCode = "401",description = "Username or password is incorrect."),
            @ApiResponse(responseCode = "404",description = "User not found.")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO){
        return ResponseEntity.ok(authService.login(loginDTO));
    }
}

