package com.ilicitan_airlines.backend.entity.flight.structure;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prices implements Serializable {
    @NotNull(message = "Economy price is required")
    @Positive(message = "Economy price must be greater than 0")
    private Double economy;

    @NotNull(message = "XXL price is required")
    @Positive(message = "XXL price must be greater than 0")
    private Double xxl;

    @NotNull(message = "Business price is required")
    @Positive(message = "Business price must be greater than 0")
    private Double business;
}