package com.buren.playlog.repository;

import com.buren.playlog.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends AbstractRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.game.rawgId = :gameId AND r.active = true")
    Optional<Review> findByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);
}
