package com.codecalibrate.domain;

public class LearningPathNotFoundException extends RuntimeException {

    public LearningPathNotFoundException(Integer id) {
        super("Could not find LearningPath with id " + id);
    }
}
