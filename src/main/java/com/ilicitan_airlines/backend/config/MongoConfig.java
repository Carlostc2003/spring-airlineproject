package com.ilicitan_airlines.backend.config;

import org.bson.Document;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Bean
    public ApplicationRunner mongoConnectionVerifier(MongoTemplate mongoTemplate) {
        return args -> mongoTemplate.executeCommand(new Document("ping", 1));
    }
}
