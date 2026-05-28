package com.ilicitan_airlines.backend.dto.booking;

import com.ilicitan_airlines.backend.dto.flight.FlightDTO;
import com.ilicitan_airlines.backend.dto.user.UserDTO;
import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing the summarized booking information")
public class BookingSummaryDTO {
    @Schema(description = "User profile information")
    private UserDTO user;

    @Schema(description = "Flight details")
    private FlightDTO flight;

    @Schema(description = "Assigned aircraft information")
    private Aircraft aircraft;

    @Schema(description = "Total calculated price of the booking", example = "299.99")
    private Double finalPrice;
}