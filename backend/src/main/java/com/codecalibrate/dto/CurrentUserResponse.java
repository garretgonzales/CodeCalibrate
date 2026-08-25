package com.codecalibrate.dto;

public record CurrentUserResponse (
        Integer id,
        String username,
        String email
)
{
}
