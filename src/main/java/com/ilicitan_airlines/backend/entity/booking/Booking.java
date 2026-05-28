package com.ilicitan_airlines.backend.entity.booking;

import java.io.Serializable;

import com.ilicitan_airlines.backend.entity.booking.structure.Details;
import com.ilicitan_airlines.backend.entity.booking.structure.SeatClass;
import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking implements Serializable {
    @Id
    private String id;
    private Details details;
    private String userId;
    private SeatClass seatClass;
    private String seatNumber;
    private String flightId;
}
