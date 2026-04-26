package com.buren.playlog.repository;

import com.buren.playlog.model.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends AbstractRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.game.rawgId = :gameId AND r.active = true")
    Optional<Review> findByUserIdAndGameId(@Param("userId") Long userId, @Param("gameId") Long gameId);

    @Query("SELECT r FROM Review r WHERE r.active = true")
    Page<Review> findAllActive(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.active = true AND r.user.id = :userId")
    Page<Review> findAllActiveByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.active = true AND r.game.rawgId = :gameId")
    Page<Review> findAllActiveByGameId(@Param("gameId") Long gameId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
