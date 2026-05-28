package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.aircraft.AircraftDTO;
import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.aircraft.AircraftService;
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
public class AircraftControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AircraftService as;

    @Mock
    private Mapper m;

    @InjectMocks
    private AircraftController aircraftController;

    @BeforeEach
    void setUp() {mockMvc = MockMvcBuilders.standaloneSetup(aircraftController).build();}

    @Test
    void shouldCreateAircraft() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"AA-123\", "
                + "\"model\":\"Boeing 737\", "
                + "\"features\":{"
                + "   \"starlink\":true, "
                + "   \"externalCameras\":true, "
                + "   \"cabinScreens\":true"
                + "}, "
                + "\"seats\":{"
                + "   \"economy\":100, "
                + "   \"xxl\":10, "
                + "   \"business\":20, "
                + "   \"total\":130"
                + "}"
                + "}";
        Aircraft aircraft = new Aircraft();
        when(m.toEntity(any(AircraftDTO.class))).thenReturn(aircraft);
        when(as.create(any(Aircraft.class))).thenReturn(aircraft);
        when(m.toDTO(any(Aircraft.class))).thenReturn(new AircraftDTO());
        mockMvc.perform(post("/api/aircrafts")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateAircraft() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"AA-123\", "
                + "\"model\":\"Boeing 737 Updated\", "
                + "\"features\":{"
                + "   \"starlink\":false, "
                + "   \"externalCameras\":true, "
                + "   \"cabinScreens\":true"
                + "}, "
                + "\"seats\":{"
                + "   \"economy\":120, "
                + "   \"xxl\":10, "
                + "   \"business\":20, "
                + "   \"total\":150"
                + "}"
                + "}";
        Aircraft aircraft = new Aircraft();
        when(m.toEntity(any(AircraftDTO.class))).thenReturn(aircraft);
        when(as.update(anyString(), any(Aircraft.class))).thenReturn(aircraft);
        when(m.toDTO(any(Aircraft.class))).thenReturn(new AircraftDTO());
        mockMvc.perform(put("/api/aircrafts/{id}", "AA-123")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldListAircrafts() throws Exception {
        when(as.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/aircrafts")).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteAircraft() throws Exception {
        mockMvc.perform(delete("/api/aircrafts/{id}", "AA-123")).andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAircraftById() throws Exception {
        Aircraft aircraft = new Aircraft();
        when(as.getById("AA-123")).thenReturn(aircraft);
        when(m.toDTO(any(Aircraft.class))).thenReturn(new AircraftDTO());
        mockMvc.perform(get("/api/aircrafts/{id}", "AA-123")).andExpect(status().isOk());
    }
}