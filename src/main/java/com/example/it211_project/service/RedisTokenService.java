package com.example.it211_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String BLACKLIST_PREFIX =
            "blacklist:access-token:";

    public void blacklistToken(
            String token,
            Date expirationDate
    ) {
        long timeToLive =
                expirationDate.getTime() - System.currentTimeMillis();

        if (timeToLive <= 0) {
            return;
        }

        stringRedisTemplate
                .opsForValue()
                .set(
                        BLACKLIST_PREFIX + token,
                        "revoked",
                        Duration.ofMillis(timeToLive)
                );
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean exists = stringRedisTemplate.hasKey(
                BLACKLIST_PREFIX + token
        );

        return Boolean.TRUE.equals(exists);
    }
}