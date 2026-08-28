package com.codecalibrate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class AttemptServiceTest {

  private AttemptRepository attemptRepository;
  private AttemptService service;

  @BeforeEach
  void setUp() {
    attemptRepository = mock(AttemptRepository.class);
    service = new AttemptService(attemptRepository);
  }

  @Test
  void shouldRecordOnlySafeAttemptMetadata() {
    User user = new User("attempt-user", "attempt-user@example.com", "not-a-real-password-hash");

    Exercise exercise =
        new Exercise(
            "java-variables-001",
            "Print an Age Variable",
            "Temporary exercise for this test.",
            null,
            "Beginner",
            "CodeCalibrate");

    service.recordAttempt(user, exercise, true);

    ArgumentCaptor<Attempt> attemptCaptor = ArgumentCaptor.forClass(Attempt.class);

    verify(attemptRepository).save(attemptCaptor.capture());
    verifyNoMoreInteractions(attemptRepository);

    Attempt savedAttempt = attemptCaptor.getValue();

    assertThat(savedAttempt.getUser()).isSameAs(user);
    assertThat(savedAttempt.getExercise()).isSameAs(exercise);
    assertThat(savedAttempt.isCorrect()).isTrue();
  }
}