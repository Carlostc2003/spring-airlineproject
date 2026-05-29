package com.ilicitan_airlines.backend.dto.flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing airports list with iata code & airport city")
public class AirportsDepDTO {
    @NotBlank(message = "Airport IATA code is required")
    @Pattern(
            regexp = Regex.AIRPORT_IATA,
            message = "Airport must be a valid IATA code (3 uppercase letters)"
    )
    @Schema(description = "departure airport IATA code", example = "MAD")
    @JsonProperty("airport_code")
    private String airportCode;

    @NotBlank(message = "Airport city is required")
    @Schema(description = "departure airport city", example = "Madrid")
    @JsonProperty("city_airport")
    private String cityAirport;
}
