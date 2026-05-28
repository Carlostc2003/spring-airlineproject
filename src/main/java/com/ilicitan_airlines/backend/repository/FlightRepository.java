package com.ilicitan_airlines.backend.repository;

import com.ilicitan_airlines.backend.entity.flight.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.Instant;
import java.util.List;

public interface FlightRepository extends MongoRepository<Flight, String> {
    List<Flight> findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsEconomyGreaterThan(String departure, String arrival, Instant start, Instant end, Integer minSeats);
    List<Flight> findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsXxlGreaterThan(String departure, String arrival, Instant start, Instant end, Integer minSeats);
    List<Flight> findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsBusinessGreaterThan(String departure, String arrival, Instant start, Instant end, Integer minSeats);
}