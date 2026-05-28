package com.ilicitan_airlines.backend.security;

import com.ilicitan_airlines.backend.config.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;
import tools.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.time.*;

@Component
public class ApiRateLimitingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ApiRateLimitingFilter.class);
    private final InMemoryRateLimiter rateLimiter;
    private final SecurityProperties securityProperties;
    private final ObjectMapper objectMapper;

    public ApiRateLimitingFilter(InMemoryRateLimiter rateLimiter, SecurityProperties securityProperties, ObjectMapper objectMapper) {
        this.rateLimiter = rateLimiter;
        this.securityProperties = securityProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String category = resolveCategory(request.getRequestURI());
        SecurityProperties.RateLimit policy = resolvePolicy(category);
        String key = buildKey(request, category);
        RateLimitDecision decision = rateLimiter.consume(key, policy.getLimit(), policy.getWindowSeconds());
        response.setHeader("X-RateLimit-Limit", String.valueOf(decision.limit()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(decision.remaining()));
        response.setHeader("X-RateLimit-Window-Seconds", String.valueOf(policy.getWindowSeconds()));
        if (!decision.allowed()) {
            response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(decision.retryAfterSeconds()));
            writeTooManyRequestsResponse(request, response);
            log.warn("Rate limit exceeded for {} {}", request.getMethod(), request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveCategory(String uri) {
        if ("/api/users/login".equals(uri) || "/api/users/login/google".equals(uri)) return "auth";
        return "api";
    }

    private SecurityProperties.RateLimit resolvePolicy(String category) {
        if ("auth".equals(category)) return securityProperties.getAuthRateLimit();
        return securityProperties.getApiRateLimit();
    }

    private String buildKey(HttpServletRequest request, String category) {
        return request.getRemoteAddr() + ":" + category;
    }

    private void writeTooManyRequestsResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, "Too many requests. Retry later.");
        problemDetail.setTitle(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getRequestURI());
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}
