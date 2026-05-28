package com.ilicitan_airlines.backend.security;

public record RateLimitDecision(boolean allowed, int limit, int remaining, long retryAfterSeconds) {}
