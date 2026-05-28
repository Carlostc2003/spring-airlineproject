package com.ilicitan_airlines.backend.service.flight;

import com.ilicitan_airlines.backend.entity.flight.Flight;
import java.time.Instant;
import java.util.List;

public interface FlightService {
    Flight create(Flight flight);
    Flight update(String id, Flight flight);
    List<Flight> list();
    void delete(String id);
    List<Flight> searchFlights(String departure, String arrival, Instant date, String seatClass);
}