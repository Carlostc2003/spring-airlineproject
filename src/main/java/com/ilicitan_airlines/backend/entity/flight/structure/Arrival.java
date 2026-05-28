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
public class Arrival implements Serializable {
    @NotBlank(message = "Airport is required")
    @Pattern(
            regexp = Regex.AIRPORT_IATA,
            message = "Airport must be a valid IATA code (3 uppercase letters)"
    )
    private String airport;

    @NotNull(message = "Arrival date is required")
    @Future(message = "Arrival date must be in the future")
    private Instant date;
}