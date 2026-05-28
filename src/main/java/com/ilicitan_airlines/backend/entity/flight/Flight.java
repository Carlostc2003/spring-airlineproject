package com.ilicitan_airlines.backend.entity.flight;

import com.ilicitan_airlines.backend.entity.flight.structure.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "flights")
public class Flight implements Serializable {
    @Id
    private String id;
    private String flightId;
    private Departure departure;
    private Arrival arrival;
    private Prices prices;
    private Seats seats;
    private Integer duration;
    private String aircraftId;
}
