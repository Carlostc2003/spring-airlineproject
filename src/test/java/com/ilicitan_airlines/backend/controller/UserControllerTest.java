package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.user.*;
import com.ilicitan_airlines.backend.entity.user.User;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.user.UserService;
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
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {mockMvc = MockMvcBuilders.standaloneSetup(userController).build();}

    @Test
    void shouldCreateUser() throws Exception {
        String jsonContent = "{"
                + "\"email\":\"test@test.com\", "
                + "\"password\":\"Password123!\", "
                + "\"name\":\"John\", "
                + "\"surname\":\"Doe\", "
                + "\"sex\":\"MALE\", "
                + "\"passport\": {\"id\":\"12345678\", \"expirationDate\":\"2030-01-01T00:00:00Z\", \"issueCountry\":\"Spain\"}"
                + "}";
        User user = new User();
        user.setEmail("test@test.com");
        when(mapper.toEntity(any(UserRegisterRequest.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(mapper.toDTO(any(User.class))).thenReturn(new UserDTO());
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)).andExpect(status().isCreated());
    }

    @Test
    void shouldListUsers() throws Exception {
        when(userService.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/users")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        String jsonContent = "{\"email\":\"test@test.com\", \"passportId\":\"12345678\", \"password\":\"Password123!\"}";
        User user = new User();
        when(userService.login(anyString(), anyString(), anyString())).thenReturn(Optional.of(user));
        when(mapper.toDTO(any(User.class))).thenReturn(new UserDTO());
        mockMvc.perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldFailGoogleLogin() throws Exception {
        mockMvc.perform(post("/api/users/login/google").contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldTransferMiles() throws Exception {
        String jsonContent = "{\"targetEmail\":\"target@test.com\", \"miles\":100}";
        mockMvc.perform(post("/api/users/{id}/transfer-miles", "user123")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldChangePassword() throws Exception {
        String jsonContent = "{\"oldPassword\":\"OldPass123!\", \"newPassword\":\"NewPass123!\"}";
        mockMvc.perform(post("/api/users/{id}/change-password", "user123")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent)).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", "user123")).andExpect(status().isNoContent());
    }
}