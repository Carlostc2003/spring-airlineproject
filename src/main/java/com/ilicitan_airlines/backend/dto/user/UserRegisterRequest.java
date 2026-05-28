package com.ilicitan_airlines.backend.dto.user;

import com.ilicitan_airlines.backend.entity.user.structure.*;
import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for new user registration")
public class UserRegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 64, message = "Name must be between 2 and 64 characters")
    @Schema(description = "User's first name", example = "John")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 128, message = "Surname must be between 2 and 128 characters")
    @Schema(description = "User's surname", example = "Doe")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;

    @NotNull(message = "Sex is required")
    @Schema(description = "User's biological sex", example = "MALE")
    private Sex sex;

    @Valid
    @NotNull(message = "Passport is required")
    @Schema(description = "Passport identification details")
    private Passport passport;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Schema(description = "User password", example = "SecurePass1#")
    private String password;
}