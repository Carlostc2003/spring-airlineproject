package com.ilicitan_airlines.backend.service;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.flight.Flight;
import com.ilicitan_airlines.backend.repository.*;
import com.ilicitan_airlines.backend.service.flight.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    @Mock
    private FlightRepository fr;

    @InjectMocks
    private FlightServiceImpl srv;

    private Flight f;

    @BeforeEach
    void setUp() {
        f = new Flight();
        f.setFlightId("FL-123");
        f.setAircraftId("AC-737");
    }

    @Test
    void create_Success() {
        when(fr.save(any(Flight.class))).thenAnswer(i -> i.getArgument(0));

        Flight res = srv.create(f);

        assertNotNull(res);
        assertEquals("FL-123", res.getFlightId());
        verify(fr, times(1)).save(f);
    }

    @Test
    void update_Success() {
        Flight upd = new Flight();
        upd.setFlightId("FL-999");
        upd.setAircraftId("AC-320");

        when(fr.findById("1")).thenReturn(Optional.of(f));
        when(fr.save(any(Flight.class))).thenAnswer(i -> i.getArgument(0));

        Flight res = srv.update("1", upd);

        assertNotNull(res);
        assertEquals("FL-999", res.getFlightId());
        assertEquals("AC-320", res.getAircraftId());
        verify(fr, times(1)).save(f);
    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenFlightNotExists() {
        when(fr.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> srv.update("invalid", f));
        verify(fr, never()).save(any(Flight.class));
    }

    @Test
    void list_Success() {
        when(fr.findAll()).thenReturn(List.of(f));

        List<Flight> res = srv.list();

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(fr, times(1)).findAll();
    }

    @Test
    void delete_Success() {
        assertDoesNotThrow(() -> srv.delete("1"));
        verify(fr, times(1)).deleteById("1");
    }

    @Test
    void searchFlights_Economy_Success() {
        Instant date = Instant.parse("2026-06-09T12:00:00Z");
        Instant start = date.truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS).minusMillis(1);

        when(fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsEconomyGreaterThan("ALC", "MAD", start, end, 0))
                .thenReturn(List.of(f));

        List<Flight> res = srv.searchFlights("ALC", "MAD", date, "ECONOMY");

        assertNotNull(res);
        assertEquals(1, res.size());
    }

    @Test
    void searchFlights_Xxl_Success() {
        Instant date = Instant.parse("2026-06-09T12:00:00Z");
        Instant start = date.truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS).minusMillis(1);

        when(fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsXxlGreaterThan("ALC", "MAD", start, end, 0))
                .thenReturn(List.of(f));

        List<Flight> res = srv.searchFlights("ALC", "MAD", date, "XXL");

        assertNotNull(res);
        assertEquals(1, res.size());
    }

    @Test
    void searchFlights_Business_Success() {
        Instant date = Instant.parse("2026-06-09T12:00:00Z");
        Instant start = date.truncatedTo(ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS).minusMillis(1);

        when(fr.findByDepartureAirportAndArrivalAirportAndDepartureDateBetweenAndSeatsBusinessGreaterThan("ALC", "MAD", start, end, 0))
                .thenReturn(List.of(f));

        List<Flight> res = srv.searchFlights("ALC", "MAD", date, "BUSINESS");

        assertNotNull(res);
        assertEquals(1, res.size());
    }

    @Test
    void searchFlights_ThrowsBadRequestException_WhenClassInvalid() {
        Instant date = Instant.now();

        assertThrows(BadRequestException.class, () -> srv.searchFlights("ALC", "MAD", date, "INVALID_CLASS"));
        verifyNoInteractions(fr);
    }
}