package com.ilicitan_airlines.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for transferring loyalty miles between users")
public class TransferMilesRequest {
    @NotBlank(message = "Target email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(description = "Recipient's email address", example = "friend@example.com")
    private String targetEmail;

    @NotNull(message = "Miles are required")
    @Positive(message = "Miles must be greater than 0")
    @Max(value = 1000000, message = "Miles exceed maximum allowed")
    @Schema(description = "Number of miles to transfer", example = "500")
    private Integer miles;
}