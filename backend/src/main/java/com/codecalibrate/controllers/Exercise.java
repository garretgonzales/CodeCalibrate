package com.codecalibrate.controllers;

import com.codecalibrate.domain.ExerciseService;
import com.codecalibrate.dto.ExerciseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/exercises")
public class Exercise {

    private final ExerciseService exerciseService;

    public Exercise(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/{id}")
    public ExerciseResponse getExerciseById(@PathVariable Integer id) {
        return exerciseService.getExerciseById(id);
    }
}
