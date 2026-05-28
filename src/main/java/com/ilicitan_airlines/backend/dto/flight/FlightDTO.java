package com.ilicitan_airlines.backend.dto.flight;

import com.ilicitan_airlines.backend.entity.flight.structure.*;
import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing flight details and configuration")
public class FlightDTO {
    @Schema(description = "Unique database identifier", example = "6650b2e3f41b2...")
    private String id;

    @NotBlank(message = "Flight ID is required")
    @Size(min = 3, max = 10, message = "Flight ID must be between 3 and 10 characters")
    @Pattern(
            regexp = Regex.FLIGHT_IATA,
            message = "Flight ID must follow format IL-100 to IL-999"
    )
    @Schema(description = "IATA Flight code", example = "IL-845")
    private String flightId;

    @Valid
    @NotNull(message = "Departure is required")
    @Schema(description = "Departure information (location and time)")
    private Departure departure;

    @Valid
    @NotNull(message = "Arrival is required")
    @Schema(description = "Arrival information (location and time)")
    private Arrival arrival;

    @Valid
    @NotNull(message = "Prices are required")
    @Schema(description = "Price structure for different seat classes")
    private Prices prices;

    @Valid
    @NotNull(message = "Seats are required")
    @Schema(description = "Configuration of available seats")
    private Seats seats;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    @Schema(description = "Flight duration in minutes", example = "120")
    private Integer duration;

    @NotBlank(message = "Aircraft ID is required")
    @Pattern(
            regexp = Regex.REGISTRATION,
            message = "Aircraft ID must follow format AA-000 (2 uppercase letters, dash, 3 digits)"
    )
    @Schema(description = "ID of the aircraft assigned to this flight", example = "AA-123")
    private String aircraftId;
}