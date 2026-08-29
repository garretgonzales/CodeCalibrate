package com.codecalibrate.controllers;

import com.codecalibrate.domain.ExerciseService;
import com.codecalibrate.domain.ExerciseSubmissionResult;
import com.codecalibrate.domain.ExerciseSubmissionService;
import com.codecalibrate.domain.RecommendationService;
import com.codecalibrate.dto.ExerciseResponse;
import com.codecalibrate.dto.ExerciseSubmissionRequest;
import com.codecalibrate.dto.ExerciseSubmissionResponse;
import com.codecalibrate.models.User;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

  private final ExerciseService exerciseService;
  private final ExerciseSubmissionService exerciseSubmissionService;
  private final RecommendationService recommendationService;

  public ExerciseController(
      ExerciseService exerciseService,
      ExerciseSubmissionService exerciseSubmissionService,
      RecommendationService recommendationService) {
    this.exerciseService = exerciseService;
    this.exerciseSubmissionService = exerciseSubmissionService;
    this.recommendationService = recommendationService;
  }

  @GetMapping("/recommended")
  public ExerciseResponse getRecommendedExercise(@AuthenticationPrincipal User user) {
    return exerciseService.getExerciseById(
        recommendationService.recommendNextExercise(user).getId());
  }

  @GetMapping("/{id}")
  public ExerciseResponse getExerciseById(@PathVariable Integer id) {
    return exerciseService.getExerciseById(id);
  }

  @PostMapping("/{id}/submissions")
  public ExerciseSubmissionResponse submitExercise(
      @PathVariable Integer id,
      @Valid @RequestBody ExerciseSubmissionRequest request,
      @AuthenticationPrincipal User user) {
    ExerciseSubmissionResult result =
        exerciseSubmissionService.submit(id, request.sourceCode(), user);

    return new ExerciseSubmissionResponse(result.correct());
  }
}