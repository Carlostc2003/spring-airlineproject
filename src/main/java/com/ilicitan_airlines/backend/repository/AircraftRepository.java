package com.ilicitan_airlines.backend.repository;

import com.ilicitan_airlines.backend.entity.aircraft.Aircraft;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends MongoRepository<Aircraft, String> {}