package com.ilicitan_airlines.backend.security;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

@Component
public class InMemoryRateLimiter {
    private final Map<String, CounterWindow> counters = new ConcurrentHashMap<>();
    private final LongSupplier clock;
    private volatile long lastCleanupAt;

    public InMemoryRateLimiter() {
        this(System::currentTimeMillis);
    }

    InMemoryRateLimiter(LongSupplier clock) {
        this.clock = clock;
    }

    public RateLimitDecision consume(String key, int limit, long windowSeconds) {
        long now = clock.getAsLong();
        long windowMillis = windowSeconds * 1000L;
        cleanup(now, windowMillis);
        CounterWindow counterWindow = counters.computeIfAbsent(key, ignored -> new CounterWindow(now));
        return counterWindow.consume(now, limit, windowMillis);
    }

    private void cleanup(long now, long windowMillis) {
        if (now - lastCleanupAt < windowMillis) return;
        synchronized (this) {
            if (now - lastCleanupAt < windowMillis) return;
            counters.entrySet().removeIf(entry -> now - entry.getValue().getLastSeenAt() > windowMillis * 2);
            lastCleanupAt = now;
        }
    }

    private static final class CounterWindow {
        private long windowStartedAt;
        private long lastSeenAt;
        private int count;

        private CounterWindow(long now) {
            this.windowStartedAt = now;
            this.lastSeenAt = now;
        }

        private synchronized RateLimitDecision consume(long now, int limit, long windowMillis) {
            if (now - windowStartedAt >= windowMillis) {windowStartedAt = now; count = 0;}
            lastSeenAt = now;
            if (count >= limit) {
                long retryAfterSeconds = Math.max(1, (windowMillis - (now - windowStartedAt) + 999) / 1000);
                return new RateLimitDecision(false, limit, 0, retryAfterSeconds);
            }
            count++;
            return new RateLimitDecision(true, limit, Math.max(0, limit - count), 0);
        }

        private long getLastSeenAt() {
            return lastSeenAt;
        }
    }
}
