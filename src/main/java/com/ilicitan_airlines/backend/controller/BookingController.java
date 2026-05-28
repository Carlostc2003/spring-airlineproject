package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.booking.BookingDTO;
import com.ilicitan_airlines.backend.entity.booking.Booking;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.booking.BookingService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "Endpoints for flight booking and management")
public class BookingController {
    private final BookingService bs;
    private final Mapper m;

    @Operation(summary = "Create booking", description = "Creates a new flight reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid booking details")
    })
    @PostMapping
    public ResponseEntity<BookingDTO> create(@RequestBody @Valid BookingDTO dto) {
        Booking created = bs.create(m.toEntity(dto));
        return new ResponseEntity<>(m.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Update booking", description = "Updates details of an existing reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> update (
            @Parameter(description = "Booking internal ID", example = "6a12c6a80241d696e5cb6cc0") @PathVariable String id,
            @RequestBody @Valid BookingDTO dto
    ) {
        Booking updated = bs.update(id, m.toEntity(dto));
        return ResponseEntity.ok(m.toDTO(updated));
    }

    @Operation(summary = "List all bookings", description = "Retrieves a list of all active reservations")
    @ApiResponse(responseCode = "200", description = "List of bookings retrieved")
    @GetMapping
    public ResponseEntity<List<BookingDTO>> list() {
        List<BookingDTO> list = bs.list().stream().map(m::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Delete booking", description = "Removes a reservation from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Booking internal ID", example = "6a12c6a80241d696e5cb6cc0") @PathVariable String id) {
        bs.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get bookings by user", description = "Retrieves all bookings associated with a specific user ID")
    @ApiResponse(responseCode = "200", description = "User bookings retrieved")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(@Parameter(description = "User internal ID", example = "6a12c6a80241d696e5cb6cbe") @PathVariable String userId) {
        List<BookingDTO> list = bs.getBookingsByUser(userId).stream().map(m::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Check-in", description = "Performs check-in for an existing booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-in successful"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "400", description = "Check-in already performed or invalid state")
    })
    @PostMapping("/{id}/check-in")
    public ResponseEntity<BookingDTO> checkIn(@Parameter(description = "Booking internal ID", example = "6a12c6a80241d696e5cb6cc0") @PathVariable String id) {
        Booking booking = bs.checkIn(id);
        return ResponseEntity.ok(m.toDTO(booking));
    }

    @Operation(summary = "Cancel booking", description = "Cancels an existing flight reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@Parameter(description = "Booking internal ID", example = "6a12c6a80241d696e5cb6cc0") @PathVariable String id) {
        Booking booking = bs.cancelBooking(id);
        return ResponseEntity.ok(m.toDTO(booking));
    }
}