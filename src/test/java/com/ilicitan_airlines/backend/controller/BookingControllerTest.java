package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.booking.BookingDTO;
import com.ilicitan_airlines.backend.entity.booking.Booking;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.booking.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookingService bs;

    @Mock
    private Mapper m;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();}

    @Test
    void shouldCreateBooking() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"b-001\", "
                + "\"userId\":\"user-123\", "
                + "\"flightId\":\"IL-100\", "
                + "\"seatClass\":\"ECONOMY\", "
                + "\"seatNumber\":\"A12\", "
                + "\"details\":{"
                + "   \"price\":150.0, "
                + "   \"status\":\"PENDING\", "
                + "   \"bookingDate\":\"2026-01-01T10:00:00Z\""
                + "}"
                + "}";
        Booking booking = new Booking();
        when(m.toEntity(any(BookingDTO.class))).thenReturn(booking);
        when(bs.create(any(Booking.class))).thenReturn(booking);
        when(m.toDTO(any(Booking.class))).thenReturn(new BookingDTO());
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"b-001\", "
                + "\"userId\":\"user-123\", "
                + "\"flightId\":\"IL-100\", "
                + "\"seatClass\":\"BUSINESS\", "
                + "\"seatNumber\":\"B1\", "
                + "\"details\":{"
                + "   \"price\":300.0, "
                + "   \"status\":\"CONFIRMED\", "
                + "   \"bookingDate\":\"2026-01-01T10:00:00Z\""
                + "}"
                + "}";
        Booking booking = new Booking();
        when(m.toEntity(any(BookingDTO.class))).thenReturn(booking);
        when(bs.update(anyString(), any(Booking.class))).thenReturn(booking);
        when(m.toDTO(any(Booking.class))).thenReturn(new BookingDTO());
        mockMvc.perform(put("/api/bookings/{id}", "b-001")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldListBookings() throws Exception {
        when(bs.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/bookings")).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        mockMvc.perform(delete("/api/bookings/{id}", "b-001")).andExpect(status().isNoContent());
    }

    @Test
    void shouldGetBookingsByUser() throws Exception {
        when(bs.getBookingsByUser("user-123")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/bookings/user/{userId}", "user-123")).andExpect(status().isOk());
    }

    @Test
    void shouldCheckInBooking() throws Exception {
        Booking booking = new Booking();
        when(bs.checkIn("b-001")).thenReturn(booking);
        when(m.toDTO(any(Booking.class))).thenReturn(new BookingDTO());
        mockMvc.perform(post("/api/bookings/{id}/check-in", "b-001")).andExpect(status().isOk());
    }

    @Test
    void shouldCancelBooking() throws Exception {
        Booking booking = new Booking();
        when(bs.cancelBooking("b-001")).thenReturn(booking);
        when(m.toDTO(any(Booking.class))).thenReturn(new BookingDTO());
        mockMvc.perform(post("/api/bookings/{id}/cancel", "b-001")).andExpect(status().isOk());
    }
}