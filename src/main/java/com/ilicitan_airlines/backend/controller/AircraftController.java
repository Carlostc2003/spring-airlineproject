package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.aircraft.*;
import com.ilicitan_airlines.backend.entity.aircraft.*;
import com.ilicitan_airlines.backend.mapper.*;
import com.ilicitan_airlines.backend.service.aircraft.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api/aircrafts")
@RequiredArgsConstructor
@Tag(name = "Aircraft Management", description = "Endpoints for aircraft management operations")
public class AircraftController {
    private final AircraftService as;
    private final Mapper m;

    @Operation(summary = "Create aircraft", description = "Registers a new aircraft in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aircraft created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid aircraft data")
    })
    @PostMapping
    public ResponseEntity<AircraftDTO> create(@RequestBody @Valid AircraftDTO dto) {
        Aircraft created = as.create(m.toEntity(dto));
        return new ResponseEntity<>(m.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Update aircraft", description = "Updates details of an existing aircraft")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aircraft updated successfully"),
            @ApiResponse(responseCode = "404", description = "Aircraft not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AircraftDTO> update (
            @Parameter(description = "Aircraft registration ID", example = "EC-843") @PathVariable String id,
            @RequestBody @Valid AircraftDTO dto
    ) {
        Aircraft updated = as.update(id, m.toEntity(dto));
        return ResponseEntity.ok(m.toDTO(updated));
    }

    @Operation(summary = "List all aircrafts", description = "Retrieves a list of all registered aircrafts")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AircraftDTO>> list() {
        List<AircraftDTO> list = as.list().stream().map(m::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Delete aircraft", description = "Removes an aircraft from the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aircraft deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Aircraft not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Aircraft registration ID", example = "EC-843") @PathVariable String id) {
        as.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get aircraft by ID", description = "Retrieves specific aircraft details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aircraft found"),
            @ApiResponse(responseCode = "404", description = "Aircraft not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AircraftDTO> getById(@Parameter(description = "Aircraft registration ID", example = "EC-843") @PathVariable String id) {
        Aircraft a = as.getById(id);
        return ResponseEntity.ok(m.toDTO(a));
    }
}