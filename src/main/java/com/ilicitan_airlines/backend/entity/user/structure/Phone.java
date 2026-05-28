package com.ilicitan_airlines.backend.entity.user.structure;

import com.ilicitan_airlines.backend.utils.Regex;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone implements Serializable {
    @NotBlank(message = "Phone prefix is required")
    @Pattern(
            regexp = Regex.PHONE_PREFIX,
            message = "Phone prefix must follow format +XXX (1 to 4 digits)"
    )
    private String prefix;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = Regex.PHONE_NUMBER,
            message = "Phone number must contain between 6 and 15 digits"
    )
    private String number;
}