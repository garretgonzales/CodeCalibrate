package com.codecalibrate.dto;

import java.util.List;

public record DashboardResponse(
    DashboardUserResponse user,
    DashboardOverviewResponse overview,
    DashboardExerciseSummaryResponse recommendedExercise,
    List<DashboardPathSkillResponse> skillMastery,
    List<DashboardPathProgressResponse> pathProgress) {}
