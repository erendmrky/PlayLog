package com.buren.playlog.controller;

import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.model.User;
import com.buren.playlog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.root}/users")
@Tag(name = "User")
public class UserController extends AbstractController<User, Long> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }
    @Operation(summary = "Gets the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "User get successfully."),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "User not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

}
