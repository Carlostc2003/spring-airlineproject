package com.ilicitan_airlines.backend.mapper;

import com.ilicitan_airlines.backend.dto.aircraft.*;
import com.ilicitan_airlines.backend.dto.booking.*;
import com.ilicitan_airlines.backend.dto.flight.*;
import com.ilicitan_airlines.backend.dto.user.*;
import com.ilicitan_airlines.backend.entity.aircraft.*;
import com.ilicitan_airlines.backend.entity.booking.*;
import com.ilicitan_airlines.backend.entity.flight.*;
import com.ilicitan_airlines.backend.entity.user.*;
import org.springframework.stereotype.*;

@Component
public class Mapper {
    public UserDTO toDTO(User entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .surname(entity.getSurname())
                .passport(entity.getPassport())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .sex(entity.getSex())
                .miles(entity.getMiles())
                .authProvider(entity.getAuthProvider())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .name(dto.getName())
                .surname(dto.getSurname())
                .passport(dto.getPassport())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .sex(dto.getSex())
                .miles(dto.getMiles())
                .authProvider(dto.getAuthProvider())
                .build();
    }

    public User toEntity(UserRegisterRequest request) {
        if (request == null) return null;
        return User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .sex(request.getSex())
                .passport(request.getPassport())
                .password(request.getPassword())
                .build();
    }

    public FlightDTO toDTO(Flight entity) {
        if (entity == null) return null;
        return FlightDTO.builder()
                .id(entity.getId())
                .flightId(entity.getFlightId())
                .departure(entity.getDeparture())
                .arrival(entity.getArrival())
                .prices(entity.getPrices())
                .seats(entity.getSeats())
                .duration(entity.getDuration())
                .aircraftId(entity.getAircraftId())
                .build();
    }

    public Flight toEntity(FlightDTO dto) {
        if (dto == null) return null;
        return Flight.builder()
                .id(dto.getId())
                .flightId(dto.getFlightId())
                .departure(dto.getDeparture())
                .arrival(dto.getArrival())
                .prices(dto.getPrices())
                .seats(dto.getSeats())
                .duration(dto.getDuration())
                .aircraftId(dto.getAircraftId())
                .build();
    }

    public BookingDTO toDTO(Booking entity) {
        if (entity == null) return null;
        return BookingDTO.builder()
                .id(entity.getId())
                .details(entity.getDetails())
                .userId(entity.getUserId())
                .seatClass(entity.getSeatClass())
                .seatNumber(entity.getSeatNumber())
                .flightId(entity.getFlightId())
                .build();
    }

    public Booking toEntity(BookingDTO dto) {
        if (dto == null) return null;
        return Booking.builder()
                .id(dto.getId())
                .details(dto.getDetails())
                .userId(dto.getUserId())
                .seatClass(dto.getSeatClass())
                .seatNumber(dto.getSeatNumber())
                .flightId(dto.getFlightId())
                .build();
    }

    public AircraftDTO toDTO(Aircraft entity) {
        if (entity == null) return null;
        return AircraftDTO.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .features(entity.getFeatures())
                .seats(entity.getSeats())
                .build();
    }

    public Aircraft toEntity(AircraftDTO dto) {
        if (dto == null) return null;
        return Aircraft.builder()
                .id(dto.getId())
                .model(dto.getModel())
                .features(dto.getFeatures())
                .seats(dto.getSeats())
                .build();
    }
}