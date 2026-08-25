package com.codecalibrate.domain;

public class ExerciseNotFoundException extends RuntimeException {

    public ExerciseNotFoundException(Integer id) {
        super("Exercise with ID " + id + " was not found.");
    }
}
