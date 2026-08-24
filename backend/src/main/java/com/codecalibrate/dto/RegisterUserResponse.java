package com.codecalibrate.dto;

public record RegisterUserResponse(
        Long id,
        String username,
        String email
) {
}
