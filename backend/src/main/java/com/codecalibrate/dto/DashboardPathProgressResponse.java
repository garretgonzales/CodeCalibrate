package com.codecalibrate.dto;

import java.util.List;

public record DashboardPathProgressResponse(
    Integer pathId,
    String name,
    String language,
    int completedExercises,
    int totalExercises,
    List<DashboardPathSkillResponse> skills) {}
