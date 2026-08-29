package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.data.UserRepository;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MasteryService {

  private final UserMasteryRepository userMasteryRepository;
  private final UserRepository userRepository;
  private final ExerciseRepository exerciseRepository;

  public MasteryService(
      UserMasteryRepository userMasteryRepository,
      UserRepository userRepository,
      ExerciseRepository exerciseRepository) {
    this.userMasteryRepository = userMasteryRepository;
    this.userRepository = userRepository;
    this.exerciseRepository = exerciseRepository;
  }

  @Transactional
  public void recordAttempt(Integer userId, Integer exerciseId, boolean correct) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("Authenticated user was not found."));

    Exercise exercise =
        exerciseRepository
            .findById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

    for (Skill skill : exercise.getSkills()) {
      UserMastery mastery =
          userMasteryRepository
              .findByUserAndSkill(user, skill)
              .orElseGet(() -> new UserMastery(user, skill));

      mastery.recordAttempt(correct);
      userMasteryRepository.save(mastery);
    }
  }
}