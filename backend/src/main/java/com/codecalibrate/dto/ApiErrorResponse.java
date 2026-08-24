package com.codecalibrate.dto;

import java.util.Map;

public record ApiErrorResponse(
        String message,
        Map<String, String> errors
) {
}
