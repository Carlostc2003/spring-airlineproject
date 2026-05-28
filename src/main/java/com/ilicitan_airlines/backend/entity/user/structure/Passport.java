package com.ilicitan_airlines.backend.entity.user.structure;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passport implements Serializable {
    @NotBlank(message = "Passport ID is required")
    @Size(min = 6, max = 20, message = "Passport ID must be between 6 and 20 characters")
    private String id;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private Instant expirationDate;

    @NotBlank(message = "Issue country is required")
    @Size(min = 2, max = 64, message = "Issue country must be between 2 and 64 characters")
    private String issueCountry;
}