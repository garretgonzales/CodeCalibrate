package com.codecalibrate.dto;

public record RegisterUserResponse(
        Integer id,
        String username,
        String email
) {
}
