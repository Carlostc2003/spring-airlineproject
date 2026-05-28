package com.ilicitan_airlines.backend.dto.user;

import com.ilicitan_airlines.backend.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for changing the user's password")
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    @Size(min = 8, max = 30, message = "Old password must be between 8 and 30 characters")
    @Schema(description = "Old user password", example = "Password123@")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 30, message = "New password must be between 8 and 30 characters")
    @Pattern(
            regexp = Regex.PASSWORD,
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Schema(description = "new user password", example = "NewSecurePass9#")
    private String newPassword;
}