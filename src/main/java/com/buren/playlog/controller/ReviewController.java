package com.buren.playlog.controller;

import com.buren.playlog.dto.ReviewRequestDTO;
import com.buren.playlog.dto.ReviewResponseDTO;
import com.buren.playlog.dto.ReviewUpdateDTO;
import com.buren.playlog.model.Review;
import com.buren.playlog.model.User;
import com.buren.playlog.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("${api.root}/reviews")
public class ReviewController extends AbstractController<Review, Long>{

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        super(reviewService);
        this.reviewService = reviewService;
    }

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
        try {
            return ResponseEntity.ok(reviewService.update(id, reviewUpdateDTO, currentUser));
        } catch (EntityNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> get(@PathVariable Long id){
        try {
            return ResponseEntity.ok(reviewService.getReview(id));
        } catch (EntityNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

}
