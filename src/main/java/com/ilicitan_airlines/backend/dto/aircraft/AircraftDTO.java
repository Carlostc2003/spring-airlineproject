package com.ilicitan_airlines.backend.dto.aircraft;

import com.ilicitan_airlines.backend.entity.aircraft.structure.*;
import com.ilicitan_airlines.backend.utils.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for aircraft information")
public class AircraftDTO {
    @NotBlank(message = "Aircraft ID is required")
    @Pattern(
            regexp = Regex.REGISTRATION,
            message = "Aircraft ID must follow format AA-000 (2 uppercase letters and 3 digits)"
    )
    @Schema(description = "Identifier registration of the aircraft", example = "EC-843")
    private String id;

    @NotBlank(message = "Model is required")
    @Size(min = 2, max = 128, message = "Model must be between 2 and 128 characters")
    @Schema(description = "Model name of the aircraft", example = "Airbus A380-800")
    private String model;

    @Valid
    @NotNull(message = "Features are required")
    @Schema(description = "List of features available on the aircraft")
    private Features features;

    @Valid
    @NotNull(message = "Seats are required")
    @Schema(description = "Configuration of seats available")
    private Seats seats;
}