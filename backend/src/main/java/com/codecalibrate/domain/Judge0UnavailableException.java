package com.codecalibrate.domain;

public class Judge0UnavailableException extends RuntimeException {

    private static final String MESSAGE = "Exercise validation is temporarily unavailable. Please try again.";

    public Judge0UnavailableException(Throwable cause) {
        super(MESSAGE, cause);
    }

}