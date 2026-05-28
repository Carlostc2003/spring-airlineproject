package com.ilicitan_airlines.backend.controller;

import com.ilicitan_airlines.backend.dto.user.*;
import com.ilicitan_airlines.backend.entity.user.User;
import com.ilicitan_airlines.backend.exception.*;
import com.ilicitan_airlines.backend.mapper.Mapper;
import com.ilicitan_airlines.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user management")
public class UserController {
    private final UserService us;
    private final Mapper m;

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserRegisterRequest request) {
        User created = us.create(m.toEntity(request));
        return new ResponseEntity<>(m.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Update user", description = "Updates existing user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update (
            @Parameter(description = "Internal user ID", example = "6a12c6a80241d696e5cb6cbe") @PathVariable String id,
            @RequestBody @Valid UserDTO dto
    ) {
        User updated = us.update(id, m.toEntity(dto));
        return ResponseEntity.ok(m.toDTO(updated));
    }

    @Operation(summary = "List all users", description = "Retrieves a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<UserDTO>> list() {
        List<UserDTO> list = us.list().stream().map(m::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Delete user", description = "Removes a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Internal user ID", example = "6a12c6a80241d696e5cb6cbe") @PathVariable String id) {
        us.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns their profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid LoginRequest request) {
        return us.login(request.getPassportId(), request.getEmail(), request.getPassword())
                .map(user -> ResponseEntity.ok(m.toDTO(user)))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }

    @Operation(summary = "Google login", description = "Initiates Google authentication (currently disabled)")
    @ApiResponse(responseCode = "400", description = "Feature not implemented")
    @PostMapping("/login/google")
    public ResponseEntity<UserDTO> loginWithGoogle(@RequestBody @Valid UserDTO dto) {
        throw new BadRequestException("Google login is disabled until server-side token verification is implemented");
    }

    @Operation(summary = "Transfer miles", description = "Transfers loyalty miles to another user's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient miles or invalid target")
    })
    @PostMapping("/{id}/transfer-miles")
    public ResponseEntity<Void> transferMiles (
            @Parameter(description = "Internal user ID", example = "6a12c6a80241d696e5cb6cbe") @PathVariable String id,
            @RequestBody @Valid TransferMilesRequest request
    ) {
        us.transferMiles(id, request.getTargetEmail(), request.getMiles());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change password", description = "Updates the user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Old password mismatch or invalid new password")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword (
            @Parameter(description = "Internal user ID", example = "6a12c6a80241d696e5cb6cbe") @PathVariable String id,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        us.changePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}