package com.ilicitan_airlines.backend.dto.booking;

import com.ilicitan_airlines.backend.entity.booking.structure.*;
import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing a flight booking request")
public class BookingDTO {
    @NotBlank(message = "Id is required")
    @Schema(description = "Unique booking ID", example = "6a12c6a80241d696e5cb6cc0")
    private String id;

    @Valid
    @NotNull(message = "Details are required")
    @Schema(description = "Detailed information about the booking (status, price, date)")
    private Details details;

    @NotBlank(message = "User ID is required")
    @Schema(description = "Unique identifier of the user", example = "6a12c6a80241d696e5cb6cbe")
    private String userId;

    @NotNull(message = "Seat class is required")
    @Schema(description = "Selected seat class", example = "ECONOMY")
    private SeatClass seatClass;

    @NotBlank(message = "Seat number is required")
    @Pattern(
            regexp = Regex.SEAT,
            message = "Seat number must follow format A1, B12, etc."
    )
    @Schema(description = "Assigned seat number", example = "A12")
    private String seatNumber;

    @NotBlank(message = "Flight ID is required")
    @Pattern(
            regexp = Regex.FLIGHT_IATA,
            message = "Flight ID must follow format IL-100 to IL-999"
    )
    @Schema(description = "IATA Flight code", example = "IL-100")
    private String flightId;
}