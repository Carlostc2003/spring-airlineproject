package com.ilicitan_airlines.backend.service;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.booking.Booking;
import com.ilicitan_airlines.backend.entity.booking.structure.*;
import com.ilicitan_airlines.backend.entity.flight.Flight;
import com.ilicitan_airlines.backend.entity.flight.structure.Seats;
import com.ilicitan_airlines.backend.entity.flight.structure.Departure;
import com.ilicitan_airlines.backend.repository.*;
import com.ilicitan_airlines.backend.service.booking.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository br;

    @Mock
    private FlightRepository fr;

    @InjectMocks
    private BookingServiceImpl srv;

    private Booking b;
    private Flight f;
    private Details d;
    private Seats s;
    private Departure dp;

    @BeforeEach
    void setUp() {
        d = new Details();
        d.setStatus(Status.PENDING);

        b = new Booking();
        b.setId("b-123");
        b.setFlightId("f-123");
        b.setSeatClass(SeatClass.ECONOMY);
        b.setDetails(d);

        s = new Seats();
        s.setEconomy(100);
        s.setXxl(10);
        s.setBusiness(5);

        dp = new Departure();
        dp.setDate(Instant.now().plus(24, ChronoUnit.HOURS));

        f = new Flight();
        f.setFlightId("f-123");
        f.setSeats(s);
        f.setDeparture(dp);
    }

    @Test
    void create_Success() {
        when(fr.findById("f-123")).thenReturn(Optional.of(f));
        when(br.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking res = srv.create(b);

        assertNotNull(res);
        assertEquals(99, f.getSeats().getEconomy());
        assertEquals(Status.PENDING, res.getDetails().getStatus());
        verify(fr, times(1)).save(f);
        verify(br, times(1)).save(b);
    }

    @Test
    void create_ThrowsResourceNotFoundException_WhenFlightNotExists() {
        when(fr.findById("f-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> srv.create(b));
        verify(br, never()).save(any(Booking.class));
    }

    @Test
    void update_Success() {
        Booking upd = new Booking();
        upd.setSeatClass(SeatClass.BUSINESS);
        upd.setSeatNumber("1A");
        upd.setDetails(d);

        when(br.findById("b-123")).thenReturn(Optional.of(b));
        when(br.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking res = srv.update("b-123", upd);

        assertNotNull(res);
        assertEquals(SeatClass.BUSINESS, res.getSeatClass());
        assertEquals("1A", res.getSeatNumber());
        verify(br, times(1)).save(b);
    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenBookingNotExists() {
        when(br.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> srv.update("invalid", b));
        verify(br, never()).save(any(Booking.class));
    }

    @Test
    void list_Success() {
        when(br.findAll()).thenReturn(List.of(b));

        List<Booking> res = srv.list();

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(br, times(1)).findAll();
    }

    @Test
    void delete_Success() {
        assertDoesNotThrow(() -> srv.delete("b-123"));
        verify(br, times(1)).deleteById("b-123");
    }

    @Test
    void getBookingsByUser_Success() {
        when(br.findByUserId("usr-123")).thenReturn(List.of(b));

        List<Booking> res = srv.getBookingsByUser("usr-123");

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(br, times(1)).findByUserId("usr-123");
    }

    @Test
    void checkIn_Success() {
        f.getDeparture().setDate(Instant.now().plus(24, ChronoUnit.HOURS));

        when(br.findById("b-123")).thenReturn(Optional.of(b));
        when(fr.findById("f-123")).thenReturn(Optional.of(f));
        when(br.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking res = srv.checkIn("b-123");

        assertNotNull(res);
        assertEquals(Status.CONFIRMED, res.getDetails().getStatus());
        assertNotNull(res.getSeatNumber());
        assertEquals(3, res.getSeatNumber().length());
        verify(br, times(1)).save(b);
    }

    @Test
    void checkIn_ThrowsBusinessRuleException_WhenTooEarly() {
        f.getDeparture().setDate(Instant.now().plus(72, ChronoUnit.HOURS));

        when(br.findById("b-123")).thenReturn(Optional.of(b));
        when(fr.findById("f-123")).thenReturn(Optional.of(f));

        assertThrows(BusinessRuleException.class, () -> srv.checkIn("b-123"));
        verify(br, never()).save(any(Booking.class));
    }

    @Test
    void cancelBooking_Success() {
        b.getDetails().setStatus(Status.CONFIRMED);

        when(br.findById("b-123")).thenReturn(Optional.of(b));
        when(fr.findById("f-123")).thenReturn(Optional.of(f));
        when(br.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking res = srv.cancelBooking("b-123");

        assertNotNull(res);
        assertEquals(Status.CANCELLED, res.getDetails().getStatus());
        assertEquals(101, f.getSeats().getEconomy());
        verify(fr, times(1)).save(f);
        verify(br, times(1)).save(b);
    }

    @Test
    void cancelBooking_ThrowsBusinessRuleException_WhenAlreadyCancelled() {
        b.getDetails().setStatus(Status.CANCELLED);

        when(br.findById("b-123")).thenReturn(Optional.of(b));

        assertThrows(BusinessRuleException.class, () -> srv.cancelBooking("b-123"));
        verify(br, never()).save(any(Booking.class));
    }
}