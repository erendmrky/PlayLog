package com.buren.playlog.timer;

import com.buren.playlog.repository.BlacklistTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupSchedule {

    private final BlacklistTokenRepository blacklistTokenRepository;

    public TokenCleanupSchedule(BlacklistTokenRepository blacklistTokenRepository) {
        this.blacklistTokenRepository = blacklistTokenRepository;
    }

    @Scheduled(cron = "${blacklist.cron}") // "0 0 0 1 * ?" (every month)
    public void clearBlackListTable(){
        blacklistTokenRepository.deleteBlackListToken();
    }
}
