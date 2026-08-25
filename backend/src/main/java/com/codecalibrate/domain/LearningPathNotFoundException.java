package com.codecalibrate.domain;

public class LearningPathNotFoundException extends RuntimeException {

    public LearningPathNotFoundException(Integer id) {
        super("Learning path with ID " + id + " was not found.");
    }
}
