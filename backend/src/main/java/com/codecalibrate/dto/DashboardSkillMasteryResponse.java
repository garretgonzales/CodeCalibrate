package com.codecalibrate.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DashboardSkillMasteryResponse(
    Integer skillId,
    String name,
    String description,
    String difficulty,
    BigDecimal masteryScore,
    int questionsAttempted,
    int questionsCorrect,
    Instant lastPracticedAt) {}
