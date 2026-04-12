package com.buren.playlog.service;

import com.buren.playlog.dto.*;
import com.buren.playlog.exceptions.RawgException;
import com.buren.playlog.model.BaseEntity;
import com.buren.playlog.model.Game;
import com.buren.playlog.model.Review;
import com.buren.playlog.model.User;
import com.buren.playlog.repository.GameRepository;
import com.buren.playlog.repository.ReviewRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
public class ReviewService extends AbstractService<Review, Long>{

    @Value("${rawg.apikey}")
    private String rawgApiKey;

    private final RestClient rawgClient;
    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository, GameRepository gameRepository, RestClient rawgClient) {
        super(reviewRepository);
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.rawgClient = rawgClient;
    }

    public Page<ReviewResponseDTO> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        return reviewRepository.findAllActive(pageable)
                .map(ReviewService::dtoFrom);
    }

    @Transactional
    public ReviewResponseDTO add(ReviewRequestDTO reviewRequestDTO, User currentUser){
        if (reviewRepository.findByUserIdAndGameId(currentUser.getId(), reviewRequestDTO.gameId()).isPresent()) {
            throw new EntityExistsException("You have already reviewed this game.");
        }
        Review review = new Review();
        review.setRating(reviewRequestDTO.rating());
        review.setComment(reviewRequestDTO.comment());
        review.setCreatedDate(LocalDate.now());
        review.setUser(currentUser);
        Game game = null;

        try {
              game = gameRepository.findByRawgId(reviewRequestDTO.gameId())
                    .filter(BaseEntity::isActive)
                    .orElseThrow(() -> new EntityNotFoundException("Game with id " + reviewRequestDTO.gameId() + " not found or inactive"));
        } catch (EntityNotFoundException _) {
            GameResponseDTO gameResponseDTO =  rawgClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("games/{id}")
                            .queryParam("key", rawgApiKey)
                            .build(reviewRequestDTO.gameId()))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                        throw new RawgException("No game found or RAWG API Error " + res.getStatusCode());
                    }))
                    .body(GameResponseDTO.class);

            if (gameResponseDTO != null) {
                game =  gameRepository.save(GameService.fromDTO(gameResponseDTO));
            }
        }
        if (game == null) {
            throw new EntityNotFoundException("Game with id " + reviewRequestDTO.gameId() + " not found");
        }
        review.setGame(game);
        return dtoFrom(abstractRepository.save(review));
    }

    @Transactional
    public ReviewResponseDTO update(Long id, ReviewUpdateDTO reviewUpdateDTO, User currentUser){
        Review review = super.get(id);
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only update your own reviews.");
        }
        review.setComment(reviewUpdateDTO.comment());
        review.setRating(reviewUpdateDTO.rating());
        return dtoFrom(abstractRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        Review review = super.get(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("You must be logged in to delete a review.");
        }

        User currentUser = (User) authentication.getPrincipal();
        if (currentUser != null) {
            if (!review.getUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("You can only delete your own reviews.");
            }
            super.delete(id);
        }
        throw new SecurityException("You must be logged in to delete a review.");
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
