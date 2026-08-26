package com.codecalibrate.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "exercise-content.github")
public record GitHubExerciseContentProperties(
        URI apiUrl,
        String repository,
        String revision,
        String exerciseDirectory,
        String token
) {
}
