package com.buren.playlog.controller;

import com.buren.playlog.dto.ReviewRequestDTO;
import com.buren.playlog.dto.ReviewResponseDTO;
import com.buren.playlog.dto.ReviewUpdateDTO;
import com.buren.playlog.model.Review;
import com.buren.playlog.model.User;
import com.buren.playlog.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("${api.root}/reviews")
@Tag(name="Review")
public class ReviewController extends AbstractController<Review, Long>{

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        super(reviewService);
        this.reviewService = reviewService;
    }

    @Operation(summary = "Gets all reviews with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Reviews retrieved successfully.")
    })
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDTO>> getAll(@PositiveOrZero @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @Positive @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(reviewService.getAll(page, size));
    }

    @Operation(summary = "Adds review to game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Review added successfully."),
            @ApiResponse(responseCode = "403",description = "Unauthorized action.")
    })
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO, @AuthenticationPrincipal User currentUser){
        ReviewResponseDTO responseDTO = reviewService.add(reviewRequestDTO, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ReviewUpdateDTO reviewUpdateDTO, @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(reviewService.update(id, reviewUpdateDTO, currentUser));
    }

    @Operation(summary = "Gets the review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Review retrieved successfully."),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "Review not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> get(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReview(id));
    }

}
