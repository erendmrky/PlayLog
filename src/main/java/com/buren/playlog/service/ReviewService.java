package com.buren.playlog.service;

import com.buren.playlog.dto.ReviewRequestDTO;
import com.buren.playlog.dto.ReviewResponseDTO;
import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.model.Review;
import com.buren.playlog.repository.GameRepository;
import com.buren.playlog.repository.ReviewRepository;
import com.buren.playlog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReviewService extends AbstractService<Review, Long>{

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository,GameRepository gameRepository) {
        super(reviewRepository);
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public ReviewResponseDTO add(ReviewRequestDTO reviewRequestDTO){

        Review review = new Review();
        review.setRating(reviewRequestDTO.rating());
        review.setComment(reviewRequestDTO.comment());
        review.setCreatedDate(LocalDate.now());
        review.setUser(userRepository.findById(reviewRequestDTO.userId())
                .filter(f -> f.isActive())
                .orElseThrow(() -> new EntityNotFoundException(getClass().getSimpleName() + " with id " + reviewRequestDTO.userId() + " not found or inactive")));
        review.setGame(gameRepository.findById(reviewRequestDTO.gameId())
                .filter(f -> f.isActive())
                .orElseThrow(() -> new EntityNotFoundException(getClass().getSimpleName() + " with id " + reviewRequestDTO.gameId() + " not found or inactive")));


        return dtoFrom(abstractRepository.save(review));
    }

    public ReviewResponseDTO getReview(Long id){
        return dtoFrom(super.get(id));
    }

    private static ReviewResponseDTO dtoFrom(Review review){
        return new ReviewResponseDTO(review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedDate(),
                new UserResponseDTO(review.getUser().getId(),review.getUser().getUsername(),review.getUser().getEmail()),
                review.getGame());
    }

}
