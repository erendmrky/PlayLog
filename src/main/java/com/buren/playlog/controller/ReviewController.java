package com.buren.playlog.controller;

import com.buren.playlog.dto.ReviewRequestDTO;
import com.buren.playlog.dto.ReviewResponseDTO;
import com.buren.playlog.model.Review;
import com.buren.playlog.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Adds review to game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Review added successfully."),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "User or game not found.")
    })
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO){
        ReviewResponseDTO responseDTO = reviewService.add(reviewRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @Operation(summary = "Gets the review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Review get successfully."),
            @ApiResponse(responseCode = "403",description = "Unauthorized action."),
            @ApiResponse(responseCode = "404",description = "Review not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> get(@PathVariable Long id){
        try {
            return ResponseEntity.ok(reviewService.getReview(id));
        } catch (EntityNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

}
