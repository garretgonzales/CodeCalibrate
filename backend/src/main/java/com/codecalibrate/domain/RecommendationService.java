package com.codecalibrate.domain;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

  private final UserMasteryRepository userMasteryRepository;
  private final ExerciseRepository exerciseRepository;
  private final AttemptRepository attemptRepository;

  public RecommendationService(
      UserMasteryRepository userMasteryRepository,
      ExerciseRepository exerciseRepository,
      AttemptRepository attemptRepository) {
    this.userMasteryRepository = userMasteryRepository;
    this.exerciseRepository = exerciseRepository;
    this.attemptRepository = attemptRepository;
  }

  public Exercise recommendNextExercise(User user) {
    List<UserMastery> masteries =
        userMasteryRepository.findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user);

    for (UserMastery mastery : masteries) {
      List<Exercise> exercises =
          exerciseRepository.findBySkillsContainingOrderByIdAsc(mastery.getSkill());

      if (!exercises.isEmpty()) {
        return exercises.getFirst();
      }
    }

    return exerciseRepository
        .findFirstByOrderByIdAsc()
        .orElseThrow(() -> new IllegalStateException("No exercises are available."));
  }
}
