package com.ilicitan_airlines.backend.repository;

import com.ilicitan_airlines.backend.entity.booking.Booking;
import com.ilicitan_airlines.backend.entity.booking.structure.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    boolean existsByUserIdAndDetailsStatus(String userId, Status status);
}