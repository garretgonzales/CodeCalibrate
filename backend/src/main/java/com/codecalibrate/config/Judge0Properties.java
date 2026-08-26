package com.codecalibrate.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "judge0")
public record Judge0Properties(
        URI apiUrl,
        String rapidApiHost,
        String rapidApiKey,
        int javaLanguageId
) {
}
