package com.codecalibrate.domain.content;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/* backend representation of GitHub content file
execution.tests has hidden validation details
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public record ExerciseContentDefinition(
        String id,
        Integer version,
        String language,
        String title,
        String description,
        String difficulty,
        String starterCode,
        Execution execution
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Execution(
            String className,
            Integer timeLimitSeconds,
            Integer memoryLimitKilobytes,
            List<TestCase> tests
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TestCase(
            String stdin,
            String expectedStdout
    ) {
    }
}

