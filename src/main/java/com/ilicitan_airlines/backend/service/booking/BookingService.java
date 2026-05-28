package com.ilicitan_airlines.backend.service.booking;

import com.ilicitan_airlines.backend.entity.booking.Booking;
import com.ilicitan_airlines.backend.entity.booking.structure.SeatClass;
import com.ilicitan_airlines.backend.entity.flight.Flight;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking);
    Booking update(String id, Booking booking);
    List<Booking> list();
    void delete(String id);
    List<Booking> getBookingsByUser(String userId);
    Booking checkIn(String bookingId);
    Booking cancelBooking(String bookingId);
}