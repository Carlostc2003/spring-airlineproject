package com.ilicitan_airlines.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationPasswordEncoderTest {
    @Test
    void shouldEncodeNewPasswordsWithPbkdf2() {
        ApplicationPasswordEncoder passwordEncoder = buildPasswordEncoder();
        String encoded = passwordEncoder.encode("Abcd1234!");

        assertTrue(encoded.startsWith("{pbkdf2}"));
        assertTrue(passwordEncoder.matches("Abcd1234!", encoded));
        assertFalse(passwordEncoder.upgradeEncoding(encoded));
    }

    @Test
    void shouldMatchLegacyBcryptAndRequireUpgrade() {
        ApplicationPasswordEncoder passwordEncoder = buildPasswordEncoder();
        String legacyHash = new BCryptPasswordEncoder(12).encode("Abcd1234!");

        assertTrue(passwordEncoder.matches("Abcd1234!", legacyHash));
        assertTrue(passwordEncoder.upgradeEncoding(legacyHash));
    }

    private ApplicationPasswordEncoder buildPasswordEncoder() {
        PasswordEncoder legacyEncoder = new BCryptPasswordEncoder(12);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("bcrypt", legacyEncoder);
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(legacyEncoder);
        return new ApplicationPasswordEncoder(delegatingPasswordEncoder, legacyEncoder);
    }
}
