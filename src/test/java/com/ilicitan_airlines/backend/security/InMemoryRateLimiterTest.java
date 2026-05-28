package com.ilicitan_airlines.backend.security;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRateLimiterTest {
    @Test
    void shouldBlockRequestsWhenLimitIsExceeded() {
        AtomicLong now = new AtomicLong(0);
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter(now::get);

        assertTrue(rateLimiter.consume("127.0.0.1:/api/users/login", 2, 60).allowed());
        assertTrue(rateLimiter.consume("127.0.0.1:/api/users/login", 2, 60).allowed());
        RateLimitDecision decision = rateLimiter.consume("127.0.0.1:/api/users/login", 2, 60);

        assertFalse(decision.allowed());
        assertTrue(decision.retryAfterSeconds() > 0);
    }

    @Test
    void shouldAllowRequestsAgainAfterWindowExpires() {
        AtomicLong now = new AtomicLong(0);
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter(now::get);

        rateLimiter.consume("127.0.0.1:/api/flights", 1, 60);
        assertFalse(rateLimiter.consume("127.0.0.1:/api/flights", 1, 60).allowed());

        now.set(61_000);

        assertTrue(rateLimiter.consume("127.0.0.1:/api/flights", 1, 60).allowed());
    }
}
