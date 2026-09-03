package com.codecalibrate.dto;

import java.util.List;

public record DashboardExerciseSummaryResponse(
    Integer id, String title, String description, String difficulty, List<String> skills) {}
