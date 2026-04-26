package com.buren.playlog.repository;

import com.buren.playlog.model.BlacklistToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BlacklistTokenRepository extends AbstractRepository<BlacklistToken,Long> {
    Optional<BlacklistToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistToken b WHERE b.active = False")
    void deleteBlackListToken();
}
