package com.ilicitan_airlines.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CorsConfigurationTest {
    @Test
    void shouldAllowAngularDevOrigins() {
        CorsProperties corsProperties = new CorsProperties();
        SecurityConfig securityConfig = new SecurityConfig();
        CorsConfigurationSource source = securityConfig.corsConfigurationSource(corsProperties);
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/users");

        CorsConfiguration configuration = source.getCorsConfiguration(request);

        assertEquals("http://localhost:4200", configuration.checkOrigin("http://localhost:4200"));
        assertEquals("http://127.0.0.1:4200", configuration.checkOrigin("http://127.0.0.1:4200"));
        assertTrue(configuration.getAllowedMethods().contains("POST"));
        assertTrue(configuration.getAllowedHeaders().contains("*"));
        assertFalse(Boolean.TRUE.equals(configuration.getAllowCredentials()));
    }

    @Test
    void shouldRejectUnknownOrigin() {
        CorsProperties corsProperties = new CorsProperties();
        SecurityConfig securityConfig = new SecurityConfig();
        CorsConfigurationSource source = securityConfig.corsConfigurationSource(corsProperties);
        MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/api/users");

        CorsConfiguration configuration = source.getCorsConfiguration(request);

        assertNull(configuration.checkOrigin("http://malicious.example"));
    }
}
