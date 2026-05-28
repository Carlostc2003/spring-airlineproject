package com.ilicitan_airlines.backend.service;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import com.ilicitan_airlines.backend.repository.*;
import com.ilicitan_airlines.backend.service.aircraft.AircraftServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AircraftServiceTest {
    @Mock
    private AircraftRepository ar;

    @InjectMocks
    private AircraftServiceImpl srv;

    private Aircraft a;

    @BeforeEach
    void setUp() {
        a = new Aircraft();
        a.setModel("Boeing 787");
    }

    @Test
    void create_Success() {
        when(ar.save(any(Aircraft.class))).thenAnswer(i -> i.getArgument(0));

        Aircraft res = srv.create(a);

        assertNotNull(res);
        assertEquals("Boeing 787", res.getModel());
        verify(ar, times(1)).save(a);
    }

    @Test
    void update_Success() {
        Aircraft upd = new Aircraft();
        upd.setModel("Airbus A350");

        when(ar.findById("1")).thenReturn(Optional.of(a));
        when(ar.save(any(Aircraft.class))).thenAnswer(i -> i.getArgument(0));

        Aircraft res = srv.update("1", upd);

        assertNotNull(res);
        assertEquals("Airbus A350", res.getModel());
        verify(ar, times(1)).save(a);
    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenAircraftNotExists() {
        when(ar.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> srv.update("invalid", a));
        verify(ar, never()).save(any(Aircraft.class));
    }

    @Test
    void list_Success() {
        when(ar.findAll()).thenReturn(List.of(a));

        List<Aircraft> res = srv.list();

        assertNotNull(res);
        assertEquals(1, res.size());
        verify(ar, times(1)).findAll();
    }

    @Test
    void delete_Success() {
        assertDoesNotThrow(() -> srv.delete("1"));
        verify(ar, times(1)).deleteById("1");
    }

    @Test
    void getById_Success() {
        when(ar.findById("1")).thenReturn(Optional.of(a));

        Aircraft res = srv.getById("1");

        assertNotNull(res);
        assertEquals("Boeing 787", res.getModel());
        verify(ar, times(1)).findById("1");
    }

    @Test
    void getById_ThrowsResourceNotFoundException_WhenAircraftNotExists() {
        when(ar.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> srv.getById("invalid"));
        verify(ar, times(1)).findById("invalid");
    }
}