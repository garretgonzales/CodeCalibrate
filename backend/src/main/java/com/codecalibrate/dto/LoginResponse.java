package com.codecalibrate.dto;

public record LoginResponse (
    Integer id,
    String username,
    String email,
    String token
) {

}
