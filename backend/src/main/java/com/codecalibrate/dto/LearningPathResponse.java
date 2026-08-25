package com.codecalibrate.dto;

import java.util.List;

public record LearningPathResponse(
        Integer id,
        String name,
        String description,
        String language,
        List<SkillResponse> skills
) {
}
