package com.ilicitan_airlines.backend.dto.user;

import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for user login authentication")
public class LoginRequest {
    @NotBlank(message = "Passport ID is required")
    @Size(min = 6, max = 20, message = "Passport ID must be between 6 and 20 characters")
    @Schema(description = "Unique passport identifier", example = "ABC123456")
    private String passportId;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(description = "User account email", example = "traveler@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Schema(description = "User password", example = "SecurePass1#")
    private String password;
}