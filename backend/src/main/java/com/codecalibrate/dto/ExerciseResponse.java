package com.codecalibrate.dto;

import java.util.List;

public record ExerciseResponse(
    Integer id,
    String externalId,
    String title,
    String description,
    String difficulty,
    String source,
    String starterCode,
    List<ExerciseReferenceResponse> references,
    List<SkillResponse> skills) {}
