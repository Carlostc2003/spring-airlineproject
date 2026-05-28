package com.ilicitan_airlines.backend.entity.flight.structure;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seats implements Serializable {
    @NotNull(message = "Economy seats are required")
    @Min(value = 0, message = "Economy seats cannot be negative")
    private Integer economy;

    @NotNull(message = "XXL seats are required")
    @Min(value = 0, message = "XXL seats cannot be negative")
    private Integer xxl;

    @NotNull(message = "Business seats are required")
    @Min(value = 0, message = "Business seats cannot be negative")
    private Integer business;
}