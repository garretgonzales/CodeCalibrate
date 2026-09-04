package com.codecalibrate.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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
  void shouldReturnLatestAttemptForEveryDistinctExercise() {
    String testId = UUID.randomUUID().toString();

    List<Exercise> attemptedExercises = new ArrayList<>();
    attemptedExercises.add(exercise);

    for (int index = 1; index <= 6; index++) {
      attemptedExercises.add(
              exerciseRepository.save(
                      new Exercise(
                              "history-exercise-" + index + "-" + testId,
                              "History Exercise " + index,
                              "Temporary exercise used to test attempt history.",
                              "Beginner",
                              "CodeCalibrate")));
    }

    for (Exercise attemptedExercise : attemptedExercises) {
      attemptRepository.save(new Attempt(user, attemptedExercise, false));
    }

    attemptRepository.flush();

    Attempt latestRepeatedAttempt =
            attemptRepository.saveAndFlush(new Attempt(user, exercise, true));

    User anotherUser =
            userRepository.save(
                    new User(
                            "history-other-" + testId,
                            "history-other-" + testId + "@example.com",
                            "not-a-real-password-hash"));

    attemptRepository.saveAndFlush(new Attempt(anotherUser, exercise, false));

    entityManager.clear();

    List<Attempt> latestAttempts =
            attemptRepository.findLatestForEachExerciseByUser(user);

    PersistenceUnitUtil persistenceUnitUtil =
            entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

    List<Integer> expectedExerciseIds =
            attemptedExercises.stream()
                              .map(Exercise::getId)
                              .toList();

    assertThat(latestAttempts).hasSize(7);

    assertThat(latestAttempts)
            .extracting(attempt -> attempt.getExercise().getId())
            .doesNotHaveDuplicates()
            .containsExactlyInAnyOrderElementsOf(expectedExerciseIds);

    assertThat(latestAttempts)
            .extracting(Attempt::getAttemptedAt)
            .isSortedAccordingTo(Comparator.reverseOrder());

    assertThat(latestAttempts)
            .filteredOn(
                    attempt ->
                            attempt.getExercise().getId().equals(exercise.getId()))
            .singleElement()
            .satisfies(
                    attempt -> {
                      assertThat(attempt.getId()).isEqualTo(latestRepeatedAttempt.getId());
                      assertThat(attempt.isCorrect()).isTrue();
                    });

    assertThat(latestAttempts)
            .allSatisfy(
                    attempt ->
                            assertThat(
                                    persistenceUnitUtil.isLoaded(attempt, "exercise"))
                                    .isTrue());
  }
}
