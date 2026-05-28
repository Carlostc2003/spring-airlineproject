package com.ilicitan_airlines.backend.service.user;

import com.ilicitan_airlines.backend.entity.user.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    User update(String id, User user);
    List<User> list();
    void delete(String id);
    Optional<User> login(String passportId, String email, String password);
    Optional<User> loginWithGoogle(User user);
    void transferMiles(String sourceUserId, String targetEmail, Integer miles);
    void changePassword(String id, String oldPassword, String newPassword);
}