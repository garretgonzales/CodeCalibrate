package com.codecalibrate.dto;

import java.util.List;

public record DashboardResponse(
    DashboardUserResponse user,
    DashboardOverviewResponse overview,
    DashboardExerciseSummaryResponse recommendedExercise,
    List<DashboardSkillMasteryResponse> skillMastery,
    List<DashboardAttemptResponse> recentAttempts,
    List<DashboardPathProgressResponse> pathProgress) {}
