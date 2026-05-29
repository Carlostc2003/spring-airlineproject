package com.ilicitan_airlines.backend.entity.flight.structure;

import com.ilicitan_airlines.backend.utils.Regex;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Departure implements Serializable {
    @NotBlank(message = "Airport is required")
    @Pattern(
            regexp = Regex.AIRPORT_IATA,
            message = "Airport must be a valid IATA code (3 uppercase letters)"
    )
    private String airport_code;

    @NotBlank(message = "City airport is required")
    private String city_airport;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in the future")
    private Instant date;

    @NotBlank(message = "Terminal is required")
    @Size(max = 2, message = "Terminal must not exceed 2 characters")
    private String terminal;

    @NotBlank(message = "Boarding gate is required")
    @Pattern(
            regexp = Regex.BOARDING_GATE,
            message = "Boarding gate must follow format like A1, B12, C123"
    )
    private String boardingGate;
}