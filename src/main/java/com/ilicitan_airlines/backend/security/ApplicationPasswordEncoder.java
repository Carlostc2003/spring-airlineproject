package com.ilicitan_airlines.backend.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ApplicationPasswordEncoder implements PasswordEncoder {
    private final PasswordEncoder modernEncoder;
    private final PasswordEncoder legacyEncoder;

    public ApplicationPasswordEncoder(PasswordEncoder modernEncoder, PasswordEncoder legacyEncoder) {
        this.modernEncoder = modernEncoder;
        this.legacyEncoder = legacyEncoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return modernEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) return false;
        if (isLegacyHash(encodedPassword)) return legacyEncoder.matches(rawPassword, encodedPassword);
        return modernEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) return true;
        if (isLegacyHash(encodedPassword)) return true;
        return modernEncoder.upgradeEncoding(encodedPassword);
    }

    private boolean isLegacyHash(String encodedPassword) {
        return !encodedPassword.startsWith("{");
    }
}
