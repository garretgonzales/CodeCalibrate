package com.codecalibrate.domain;

import com.codecalibrate.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "c29tZS10ZXN0LXNlY3JldC1rZXktdGhhdC1pcy1hdC1sZWFzdC0zMi1ieXRlcy1sb25n",
            3_600_000
    );

    @Test
    void shouldGenerateValidTokenContainingUserEmail() {
        User user = new User(
                "testuser",
                "test@example.com",
                "not-used-by-this-test"
        );

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token));
        assertEquals("test@example.com", jwtService.extractEmail(token));
    }

    @Test
    void shouldRejectModifiedToken() {
        User user = new User(
                "testuser",
                "test@example.com",
                "not-used-by-this-test"
        );

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.isTokenValid(token + "modified"));
    }
}
