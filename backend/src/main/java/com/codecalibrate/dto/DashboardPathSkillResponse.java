package com.codecalibrate.dto;

import java.math.BigDecimal;

public record DashboardPathSkillResponse(
    Integer skillId,
    String name,
    boolean practiced,
    BigDecimal masteryScore,
    int completedExercises,
    int totalExercises) {}
