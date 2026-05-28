package com.ilicitan_airlines.backend.service;

import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.entity.user.User;
import com.ilicitan_airlines.backend.entity.booking.structure.Status;
import com.ilicitan_airlines.backend.repository.*;
import com.ilicitan_airlines.backend.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository ur;

    @Mock
    private BookingRepository br;

    @Mock
    private PasswordEncoder psw;

    @InjectMocks
    private UserServiceImpl us;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("usr-123");
        user.setEmail("carlos@test.com");
        user.setPassword("plainPassword");
        user.setMiles(1000);
        user.setName("Carlos");
    }

    @Test
    void create_Success() {
        when(ur.existsByEmail(user.getEmail())).thenReturn(false);
        when(psw.encode("plainPassword")).thenReturn("encodedPassword");
        when(ur.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = us.create(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(0, result.getMiles());
        assertEquals("LOCAL", result.getAuthProvider());
        verify(ur, times(1)).save(user);
    }

    @Test
    void create_ThrowsConflictException_WhenEmailExists() {
        when(ur.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> us.create(user));
        verify(ur, never()).save(any(User.class));
    }

    @Test
    void update_Success() {
        User updatedFields = new User();
        updatedFields.setName("Carlos Updated");
        updatedFields.setPassword("newPlainPassword");

        when(ur.findById("usr-123")).thenReturn(Optional.of(user));
        when(psw.encode("newPlainPassword")).thenReturn("newEncodedPassword");
        when(ur.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = us.update("usr-123", updatedFields);

        assertNotNull(result);
        assertEquals("Carlos Updated", result.getName());
        assertEquals("newEncodedPassword", result.getPassword());
        verify(ur, times(1)).save(user);
    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenUserNotExists() {
        when(ur.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> us.update("invalid-id", user));
        verify(ur, never()).save(any(User.class));
    }

    @Test
    void list_Success() {
        when(ur.findAll()).thenReturn(List.of(user));

        List<User> result = us.list();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ur, times(1)).findAll();
    }

    @Test
    void delete_Success() {
        when(br.existsByUserIdAndDetailsStatus("usr-123", Status.CONFIRMED)).thenReturn(false);
        when(br.existsByUserIdAndDetailsStatus("usr-123", Status.PENDING)).thenReturn(false);

        assertDoesNotThrow(() -> us.delete("usr-123"));
        verify(ur, times(1)).deleteById("usr-123");
    }

    @Test
    void delete_ThrowsBusinessRuleException_WhenActiveBookingsExist() {
        when(br.existsByUserIdAndDetailsStatus("usr-123", Status.CONFIRMED)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> us.delete("usr-123"));
        verify(ur, never()).deleteById(anyString());
    }

    @Test
    void login_Success() {
        when(ur.findByPassportIdAndEmail("PASSPORT1", "carlos@test.com")).thenReturn(Optional.of(user));
        when(psw.matches("plainPassword", user.getPassword())).thenReturn(true);
        when(psw.upgradeEncoding(user.getPassword())).thenReturn(false);

        Optional<User> result = us.login("PASSPORT1", "carlos@test.com", "plainPassword");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void loginWithGoogle_ExistingUser() {
        when(ur.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> result = us.loginWithGoogle(user);

        assertTrue(result.isPresent());
        verify(ur, never()).save(any(User.class));
    }

    @Test
    void loginWithGoogle_NewUser() {
        when(ur.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(ur.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<User> result = us.loginWithGoogle(user);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getMiles());
        assertEquals("GOOGLE", result.get().getAuthProvider());
        verify(ur, times(1)).save(user);
    }

    @Test
    void transferMiles_Success() {
        User targetUser = new User();
        targetUser.setEmail("target@test.com");
        targetUser.setMiles(500);

        when(ur.findById("usr-123")).thenReturn(Optional.of(user));
        when(ur.findByEmail("target@test.com")).thenReturn(Optional.of(targetUser));

        assertDoesNotThrow(() -> us.transferMiles("usr-123", "target@test.com", 400));
        assertEquals(600, user.getMiles());
        assertEquals(900, targetUser.getMiles());
        verify(ur, times(1)).save(user);
        verify(ur, times(1)).save(targetUser);
    }

    @Test
    void transferMiles_ThrowsBusinessRuleException_WhenInsufficientMiles() {
        User targetUser = new User();
        when(ur.findById("usr-123")).thenReturn(Optional.of(user));

        assertThrows(BusinessRuleException.class, () -> us.transferMiles("usr-123", "target@test.com", 2000));
        verify(ur, never()).save(any(User.class));
    }

    @Test
    void transferMiles_ThrowsBadRequestException_WhenMilesNegativeOrNull() {
        when(ur.findById("usr-123")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> us.transferMiles("usr-123", "target@test.com", -50));
        verify(ur, never()).save(any(User.class));
    }

    @Test
    void changePassword_Success() {
        when(ur.findById("usr-123")).thenReturn(Optional.of(user));
        when(psw.matches("plainPassword", user.getPassword())).thenReturn(true);
        when(psw.encode("newPassword")).thenReturn("newEncodedPassword");

        assertDoesNotThrow(() -> us.changePassword("usr-123", "plainPassword", "newPassword"));
        assertEquals("newEncodedPassword", user.getPassword());
        verify(ur, times(1)).save(user);
    }

    @Test
    void changePassword_ThrowsBadRequestException_WhenOldPasswordIncorrect() {
        when(ur.findById("usr-123")).thenReturn(Optional.of(user));
        when(psw.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> us.changePassword("usr-123", "wrongPassword", "newPassword"));
        verify(ur, never()).save(any(User.class));
    }
}