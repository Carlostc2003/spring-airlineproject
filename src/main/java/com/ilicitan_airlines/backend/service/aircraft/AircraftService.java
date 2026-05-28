package com.ilicitan_airlines.backend.service.aircraft;

import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import java.util.List;

public interface AircraftService {
    Aircraft create(Aircraft aircraft);
    Aircraft update(String id, Aircraft aircraft);
    List<Aircraft> list();
    void delete(String id);
    Aircraft getById(String id);
}