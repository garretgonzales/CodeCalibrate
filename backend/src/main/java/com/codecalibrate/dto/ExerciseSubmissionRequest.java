package com.codecalibrate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExerciseSubmissionRequest(
        @NotBlank(message = "Source code is required.")
        @Size(
                max = 20000,
                message = "Source code must not exceed 20000 characters."
        )
        String sourceCode
) {


}
