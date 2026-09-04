package com.codecalibrate.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
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

  @Test
  void shouldReportWhetherUserAttemptedExercise() {
    assertThat(attemptRepository.existsByUserAndExercise(user, exercise)).isFalse();

    attemptRepository.saveAndFlush(new Attempt(user, exercise, false));

    assertThat(attemptRepository.existsByUserAndExercise(user, exercise)).isTrue();
  }

  @Test
  void shouldCountOnlyTheRequestedUsersAttempts() {
    String testId = UUID.randomUUID().toString();

    User anotherUser =
        userRepository.save(
            new User(
                "other-" + testId,
                "other-attempt-user-" + testId + "@example.com",
                "not-a-real-password-hash"));

    attemptRepository.saveAllAndFlush(
        List.of(
            new Attempt(user, exercise, true),
            new Attempt(user, exercise, true),
            new Attempt(user, exercise, false),
            new Attempt(anotherUser, exercise, true)));

    assertThat(attemptRepository.countByUser(user)).isEqualTo(3);
    assertThat(attemptRepository.countByUserAndCorrectTrue(user)).isEqualTo(2);
  }

  @Test
  void shouldReturnDistinctCorrectExerciseIdsForTheRequestedUser() {
    String testId = UUID.randomUUID().toString();

    Exercise anotherExercise =
        exerciseRepository.save(
            new Exercise(
                "another-attempt-exercise-" + testId,
                "Another Attempt Test Exercise",
                "Another temporary exercise used by this persistence test.",
                "Beginner",
                "CodeCalibrate"));

    attemptRepository.saveAllAndFlush(
        List.of(
            new Attempt(user, exercise, true),
            new Attempt(user, exercise, true),
            new Attempt(user, anotherExercise, false),
            new Attempt(user, anotherExercise, true)));

    assertThat(attemptRepository.findDistinctCorrectExerciseIdsByUser(user))
        .containsExactlyInAnyOrder(exercise.getId(), anotherExercise.getId());
  }

  @Test
  void shouldReturnFiveMostRecentAttemptsWithExercisesLoaded() {
    for (int index = 0; index < 6; index++) {
      attemptRepository.save(new Attempt(user, exercise, index % 2 == 0));
    }

    attemptRepository.flush();
    entityManager.clear();

    List<Attempt> recentAttempts = attemptRepository.findTop5ByUserOrderByAttemptedAtDesc(user);

    PersistenceUnitUtil persistenceUnitUtil =
        entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

    assertThat(recentAttempts).hasSize(5);
    assertThat(recentAttempts)
        .extracting(Attempt::getAttemptedAt)
        .isSortedAccordingTo(Comparator.reverseOrder());
    assertThat(recentAttempts)
        .allSatisfy(
            attempt -> assertThat(persistenceUnitUtil.isLoaded(attempt, "exercise")).isTrue());
  }
}
