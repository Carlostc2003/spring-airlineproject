package com.ilicitan_airlines.backend.service.flight;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.flight.*;
import com.ilicitan_airlines.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository fr;

    @Override
    @Transactional
    public Flight create(Flight flight) {
        return fr.save(flight);
    }

    @Override
    @Transactional
    public Flight update(String id, Flight flight) {
        Flight existing = fr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        existing.setFlightId(flight.getFlightId());
        existing.setDeparture(flight.getDeparture());
        existing.setArrival(flight.getArrival());
        existing.setPrices(flight.getPrices());
        existing.setSeats(flight.getSeats());
        existing.setDuration(flight.getDuration());
        existing.setAircraftId(flight.getAircraftId());
        return fr.save(existing);
    }

    @Override
    public List<Flight> list() {
        return fr.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) {
        fr.deleteById(id);
    }

    @Override
    public List<Flight> searchFlights(String departure, String arrival, Instant date, String seatClass) {
        Instant startOfDay = date.truncatedTo(ChronoUnit.DAYS);
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS).minusMillis(1);

        return switch (seatClass.toUpperCase()) {
            case "ECONOMY" ->
                    fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsEconomyGreaterThan
                            (departure, arrival, startOfDay, endOfDay, 0);
            case "XXL" ->
                    fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsXxlGreaterThan
                            (departure, arrival, startOfDay, endOfDay, 0);
            case "BUSINESS" ->
                    fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsBusinessGreaterThan
                            (departure, arrival, startOfDay, endOfDay, 0);
            default -> throw new BadRequestException("Invalid seat class");
        };
    }
}
