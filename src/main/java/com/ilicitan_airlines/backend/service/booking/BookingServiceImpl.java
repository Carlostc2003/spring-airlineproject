package com.ilicitan_airlines.backend.service.booking;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.booking.*;
import com.ilicitan_airlines.backend.entity.booking.structure.*;
import com.ilicitan_airlines.backend.entity.flight.*;
import com.ilicitan_airlines.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository br;
    private final FlightRepository fr;

    @Override
    @Transactional
    public Booking create(Booking booking) {
        Flight flight = fr.findById(booking.getFlightId()).orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        updateSeatInventory(flight, booking.getSeatClass(), -1);
        booking.getDetails().setBookingDate(Instant.now());
        booking.getDetails().setStatus(Status.PENDING);
        return br.save(booking);
    }

    @Override
    @Transactional
    public Booking update(String id, Booking booking) {
        Booking existing = br.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        existing.setDetails(booking.getDetails());
        existing.setSeatClass(booking.getSeatClass());
        existing.setSeatNumber(booking.getSeatNumber());
        return br.save(existing);
    }

    @Override
    public List<Booking> list() {
        return br.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) {
        br.deleteById(id);
    }

    @Override
    public List<Booking> getBookingsByUser(String userId) {
        return br.findByUserId(userId);
    }

    @Override
    @Transactional
    public Booking checkIn(String bookingId) {
        Booking booking = br.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        Flight flight = fr.findById(booking.getFlightId()).orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        long hoursToDeparture = ChronoUnit.HOURS.between(Instant.now(), flight.getDeparture().getDate());
        if (hoursToDeparture > 48) throw new BusinessRuleException("Check-in only available 48 hours before departure");
        booking.getDetails().setStatus(Status.CONFIRMED);
        booking.setSeatNumber(UUID.randomUUID().toString().substring(0, 3).toUpperCase());
        return br.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(String bookingId) {
        Booking booking = br.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getDetails().getStatus() == Status.CANCELLED) throw new BusinessRuleException("Booking is already cancelled");
        Flight flight = fr.findById(booking.getFlightId()).orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        updateSeatInventory(flight, booking.getSeatClass(), 1);
        booking.getDetails().setStatus(Status.CANCELLED);
        return br.save(booking);
    }

    @Transactional
    private void updateSeatInventory(Flight flight, SeatClass seatClass, int change) {
        if (seatClass == SeatClass.ECONOMY) flight.getSeats().setEconomy(flight.getSeats().getEconomy() + change);
        else if (seatClass == SeatClass.XXL) flight.getSeats().setXxl(flight.getSeats().getXxl() + change);
        else if (seatClass == SeatClass.BUSINESS) flight.getSeats().setBusiness(flight.getSeats().getBusiness() + change);
        fr.save(flight);
    }
}
