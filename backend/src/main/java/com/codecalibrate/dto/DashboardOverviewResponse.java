package com.codecalibrate.dto;

import java.math.BigDecimal;

public record DashboardOverviewResponse(
    long totalAttempts,
    long correctAttempts,
    long completedExercises,
    BigDecimal accuracy,
    BigDecimal averageMastery) {}
