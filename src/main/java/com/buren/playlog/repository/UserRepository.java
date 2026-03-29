package com.buren.playlog.repository;

import com.buren.playlog.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AbstractRepository<User, Long> {
}
