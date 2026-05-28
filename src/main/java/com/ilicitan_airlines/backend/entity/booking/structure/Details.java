package com.ilicitan_airlines.backend.entity.booking.structure;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Details implements Serializable {
    @NotNull(message = "Booking date is required")
    @PastOrPresent(message = "Booking date must be in the past or present")
    private Instant bookingDate;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Status is required")
    private Status status;
}