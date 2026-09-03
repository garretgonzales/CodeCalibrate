package com.codecalibrate.dto;

import java.time.Instant;

public record DashboardAttemptResponse(
    Integer exerciseId, String exerciseTitle, boolean correct, Instant attemptedAt) {}
