package com.buren.playlog.repository;

import com.buren.playlog.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends AbstractRepository<Review, Long> {
}
