package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.flight.FlightDTO;
import com.ilicitan_airlines.backend.entity.flight.Flight;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.flight.FlightService;
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
public class FlightControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FlightService fs;

    @Mock
    private Mapper m;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();}

    @Test
    void shouldCreateFlight() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"f-001\", "
                + "\"flightId\":\"IL-100\", "
                + "\"departure\":{\"airport\":\"MAD\", \"date\":\"2026-06-09T10:00:00Z\", \"terminal\":\"1\", \"boardingGate\":\"A1\"}, "
                + "\"arrival\":{\"airport\":\"JED\", \"date\":\"2026-06-09T23:40:00Z\"}, "
                + "\"prices\":{\"economy\":100.0, \"xxl\":200.0, \"business\":300.0}, "
                + "\"seats\":{\"economy\":100, \"xxl\":10, \"business\":10}, "
                + "\"duration\":120, "
                + "\"aircraftId\":\"AB-123\""
                + "}";

        Flight flight = new Flight();
        when(m.toEntity(any(FlightDTO.class))).thenReturn(flight);
        when(fs.create(any(Flight.class))).thenReturn(flight);
        when(m.toDTO(any(Flight.class))).thenReturn(new FlightDTO());
        mockMvc.perform(post("/api/flights").contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)).andExpect(status().isCreated());
    }

    @Test
    void shouldListFlights() throws Exception {
        when(fs.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/flights")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void shouldUpdateFlight() throws Exception {
        String jsonContent = "{"
                + "\"id\":\"f-001\", "
                + "\"flightId\":\"IL-100\", "
                + "\"departure\":{\"airport\":\"MAD\", \"date\":\"2026-06-09T10:00:00Z\", \"terminal\":\"1\", \"boardingGate\":\"A1\"}, "
                + "\"arrival\":{\"airport\":\"JED\", \"date\":\"2026-06-09T23:40:00Z\"}, "
                + "\"prices\":{\"economy\":100.0, \"xxl\":200.0, \"business\":300.0}, "
                + "\"seats\":{\"economy\":100, \"xxl\":10, \"business\":10}, "
                + "\"duration\":120, "
                + "\"aircraftId\":\"AB-123\""
                + "}";

        Flight flight = new Flight();
        when(m.toEntity(any(FlightDTO.class))).thenReturn(flight);
        when(fs.update(anyString(), any(Flight.class))).thenReturn(flight);
        when(m.toDTO(any(Flight.class))).thenReturn(new FlightDTO());
        mockMvc.perform(put("/api/flights/{id}", "flight123")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteFlight() throws Exception {
        mockMvc.perform(delete("/api/flights/{id}", "flight123")).andExpect(status().isNoContent());
    }

    @Test
    void shouldSearchFlights() throws Exception {
        when(fs.searchFlights(anyString(), anyString(), any(), anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/flights/search")
                        .param("departure", "MAD")
                        .param("arrival", "JED")
                        .param("date", "2026-06-09T10:00:00Z")
                        .param("seatClass", "ECONOMY"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }
}