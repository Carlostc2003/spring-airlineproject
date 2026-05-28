package com.ilicitan_airlines.backend.service.user;

import com.ilicitan_airlines.backend.exception.BadRequestException;
import com.ilicitan_airlines.backend.exception.BusinessRuleException;
import com.ilicitan_airlines.backend.exception.ConflictException;
import com.ilicitan_airlines.backend.exception.ResourceNotFoundException;
import com.ilicitan_airlines.backend.entity.user.User;
import com.ilicitan_airlines.backend.entity.booking.structure.Status;
import com.ilicitan_airlines.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository ur;
    private final BookingRepository br;
    private final PasswordEncoder psw;

    @Override
    @Transactional
    public User create(User user) {
        if (ur.existsByEmail(user.getEmail())) throw new ConflictException("Email already registered");
        if (user.getPassword() != null && !user.getPassword().isEmpty()) user.setPassword(psw.encode(user.getPassword()));
        user.setMiles(0);
        user.setAuthProvider("LOCAL");
        return ur.save(user);
    }

    @Override
    @Transactional
    public User update(String id, User user) {
        User existing = ur.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setPassport(user.getPassport());
        existing.setPhone(user.getPhone());
        existing.setSex(user.getSex());
        if (user.getBirthDate() != null) existing.setBirthDate(user.getBirthDate());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) existing.setPassword(psw.encode(user.getPassword()));
        return ur.save(existing);
    }

    @Override
    public List<User> list() {
        return ur.findAll();
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (br.existsByUserIdAndDetailsStatus(id, Status.CONFIRMED) ||
                br.existsByUserIdAndDetailsStatus(id, Status.PENDING)) {
            throw new BusinessRuleException("Cannot delete account with active bookings");
        }
        ur.deleteById(id);
    }

    @Override
    public Optional<User> login(String passportId, String email, String password) {
        return ur.findByPassportIdAndEmail(passportId, email)
                .filter(u -> psw.matches(password, u.getPassword()))
                .map(user -> {
                    if (psw.upgradeEncoding(user.getPassword())) {
                        user.setPassword(psw.encode(password));
                        ur.save(user);
                    }
                    return user;
                });
    }

    @Override
    public Optional<User> loginWithGoogle(User user) {
        return ur.findByEmail(user.getEmail())
                .map(Optional::of)
                .orElseGet(() -> {
                    user.setMiles(0);
                    user.setAuthProvider("GOOGLE");
                    return Optional.of(ur.save(user));
                });
    }

    @Override
    @Transactional
    public void transferMiles(String sourceUserId, String targetEmail, Integer miles) {
        User source = ur.findById(sourceUserId).orElseThrow(() -> new ResourceNotFoundException("Source user not found"));
        if (miles == null || miles <= 0) throw new BadRequestException("Miles must be greater than 0");
        if (source.getMiles() < miles) throw new BusinessRuleException("Insufficient miles");
        User target = ur.findByEmail(targetEmail).orElseThrow(() -> new ResourceNotFoundException("Target user not found"));
        source.setMiles(source.getMiles() - miles);
        target.setMiles(target.getMiles() + miles);
        ur.save(source);
        ur.save(target);
    }

    @Override
    @Transactional
    public void changePassword(String id, String oldPassword, String newPassword) {
        User user = ur.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!psw.matches(oldPassword, user.getPassword())) throw new BadRequestException("Incorrect old password");
        user.setPassword(psw.encode(newPassword));
        ur.save(user);
    }
}
