package com.ilicitan_airlines.backend.repository;

import com.ilicitan_airlines.backend.dto.flight.AirportsDepDTO;
import com.ilicitan_airlines.backend.entity.flight.Flight;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.Instant;
import java.util.List;

public interface FlightRepository extends MongoRepository<Flight, String> {

    @Query("{ 'departure.airport_code': ?0, 'arrival.airport_code': ?1, 'departure.date': { '$gte': ?2, '$lte': ?3 }, 'seats.economy': { '$gt': ?4 } }")
    List<Flight> findFlightsEconomy(String departure, String arrival, Instant start, Instant end, Integer minSeats);

    @Query("{ 'departure.airport_code': ?0, 'arrival.airport_code': ?1, 'departure.date': { '$gte': ?2, '$lte': ?3 }, 'seats.xxl': { '$gt': ?4 } }")
    List<Flight> findFlightsXxl(String departure, String arrival, Instant start, Instant end, Integer minSeats);

    @Query("{ 'departure.airport_code': ?0, 'arrival.airport_code': ?1, 'departure.date': { '$gte': ?2, '$lte': ?3 }, 'seats.business': { '$gt': ?4 } }")
    List<Flight> findFlightsBusiness(String departure, String arrival, Instant start, Instant end, Integer minSeats);

    @Aggregation(pipeline = {
            "{ '$group': { '_id': { 'airport_code': '$departure.airport_code', 'city_airport': '$departure.city_airport' } } }",
            "{ '$project': { 'airportCode': '$_id.airport_code', 'cityAirport': '$_id.city_airport', '_id': 0 } }"
    })
    List<AirportsDepDTO> getAirportsDep();
}