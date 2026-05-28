package com.ilicitan_airlines.backend.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.*;
import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private List<String> allowedOrigins = new ArrayList<>(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
    private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    private List<String> allowedHeaders = new ArrayList<>(List.of("*"));
    private boolean allowCredentials;
    private Long maxAge = 3600L;
}
