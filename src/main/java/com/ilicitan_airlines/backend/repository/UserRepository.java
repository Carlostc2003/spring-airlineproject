package com.ilicitan_airlines.backend.repository;

import com.ilicitan_airlines.backend.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPassportIdAndEmail(String passportId, String email);
    boolean existsByEmail(String email);
}