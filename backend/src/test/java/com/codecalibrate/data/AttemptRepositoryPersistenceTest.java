package com.codecalibrate.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class AttemptRepositoryPersistenceTest {

  @Autowired private AttemptRepository attemptRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private ExerciseRepository exerciseRepository;

  @Autowired private EntityManager entityManager;

  private User user;
  private Exercise exercise;

  @BeforeEach
  void setUp() {
    String testId = UUID.randomUUID().toString();

    user =
        userRepository.save(
            new User(
                "attempt-user-" + testId,
                "attempt-user-" + testId + "@example.com",
                "not-a-real-password-hash"));

    exercise =
        exerciseRepository.save(
            new Exercise(
                "attempt-exercise-" + testId,
                "Attempt Test Exercise",
                "Temporary exercise used only by this persistence test.",
                null,
                "Beginner",
                "CodeCalibrate"));
  }

  @Test
  void shouldPersistSafeAttemptMetadata() {
    Attempt savedAttempt = attemptRepository.saveAndFlush(new Attempt(user, exercise, true));

    entityManager.clear();

    Attempt foundAttempt = attemptRepository.findById(savedAttempt.getId()).orElseThrow();

    assertThat(foundAttempt.getUser().getId()).isEqualTo(user.getId());
    assertThat(foundAttempt.getExercise().getId()).isEqualTo(exercise.getId());
    assertThat(foundAttempt.isCorrect()).isTrue();
    assertThat(foundAttempt.getAttemptedAt()).isNotNull();
  }
}