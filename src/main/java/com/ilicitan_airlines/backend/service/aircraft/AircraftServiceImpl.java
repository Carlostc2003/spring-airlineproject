package com.ilicitan_airlines.backend.service.aircraft;

import com.ilicitan_airlines.backend.exception.ResourceNotFoundException;
import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import com.ilicitan_airlines.backend.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository ar;

    @Override
    @Transactional
    public Aircraft create(Aircraft aircraft) {
        return ar.save(aircraft);
    }

    @Override
    @Transactional
    public Aircraft update(String id, Aircraft aircraft) {
        Aircraft existing = ar.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aircraft not found"));
        existing.setModel(aircraft.getModel());
        existing.setFeatures(aircraft.getFeatures());
        existing.setSeats(aircraft.getSeats());
        return ar.save(existing);
    }

    @Override
    public List<Aircraft> list() {
        return ar.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) {
        ar.deleteById(id);
    }

    @Override
    public Aircraft getById(String id) {
        return ar.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aircraft not found"));
    }
}
