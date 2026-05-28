package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.flight.FlightDTO;
import com.ilicitan_airlines.backend.entity.flight.Flight;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.flight.FlightService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Flight Management", description = "Endpoints for flight operations")
public class FlightController {
    private final FlightService fs;
    private final Mapper m;

    @Operation(summary = "Create flight", description = "Registers a new flight in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid flight data")
    })
    @PostMapping
    public ResponseEntity<FlightDTO> create(@RequestBody @Valid FlightDTO dto) {
        Flight created = fs.create(m.toEntity(dto));
        return new ResponseEntity<>(m.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Update flight", description = "Updates details of an existing flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight updated successfully"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> update (
            @Parameter(description = "Internal flight ID", example = "6a12c6a80241d696e5cb6cbf") @PathVariable String id,
            @RequestBody @Valid FlightDTO dto
    ) {
        Flight updated = fs.update(id, m.toEntity(dto));
        return ResponseEntity.ok(m.toDTO(updated));
    }

    @Operation(summary = "List all flights", description = "Retrieves a comprehensive list of all flights")
    @ApiResponse(responseCode = "200", description = "List of flights returned")
    @GetMapping
    public ResponseEntity<List<FlightDTO>> list() {
        List<FlightDTO> list = fs.list().stream().map(m::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Delete flight", description = "Removes a flight from the system by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Internal flight ID", example = "6a12c6a80241d696e5cb6cbf") @PathVariable String id) {
        fs.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search flights", description = "Finds flights based on route, date, and seat class")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flights found"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @Parameter(description = "Departure airport code", example = "MAD") @RequestParam String departure,
            @Parameter(description = "Arrival airport code", example = "SIN") @RequestParam String arrival,
            @Parameter(description = "Flight date in ISO-8601 format", example = "2026-06-09T00:00:00Z") @RequestParam String date,
            @Parameter(description = "Preferred seat class", example = "ECONOMY") @RequestParam String seatClass) {
        List<FlightDTO> flights = fs.searchFlights(departure, arrival, Instant.parse(date), seatClass).stream().map(m::toDTO).toList();
        return ResponseEntity.ok(flights);
    }
}