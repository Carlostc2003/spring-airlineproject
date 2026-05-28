package com.ilicitan_airlines.backend.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private final RateLimit apiRateLimit = new RateLimit(120, 60);
    private final RateLimit authRateLimit = new RateLimit(10, 60);

    @Setter
    @Getter
    public static class RateLimit {
        private int limit;
        private long windowSeconds;

        public RateLimit() {}

        public RateLimit(int limit, long windowSeconds) {
            this.limit = limit;
            this.windowSeconds = windowSeconds;
        }

    }
}
