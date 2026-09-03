package com.codecalibrate.dto;

import java.time.Instant;

public record DashboardUserResponse(String username, String email, Instant memberSince) {}
