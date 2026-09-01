package com.codecalibrate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.domain.judge.Judge0Client;
import com.codecalibrate.domain.judge.Judge0SubmissionStatus;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExerciseSubmissionServiceTest {

  private static final Integer EXERCISE_ID = 1;
  private static final String SOURCE_CODE = "public class Main {}";

  private ExerciseRepository exerciseRepository;
  private GitHubExerciseContentClient gitHubExerciseContentClient;
  private Judge0Client judge0Client;
  private ExerciseSubmissionService service;
  private AttemptService attemptService;

  @BeforeEach
  void setUp() {
    exerciseRepository = mock(ExerciseRepository.class);
    gitHubExerciseContentClient = mock(GitHubExerciseContentClient.class);
    judge0Client = mock(Judge0Client.class);
    attemptService = mock(AttemptService.class);

    service =
        new ExerciseSubmissionService(
            exerciseRepository, gitHubExerciseContentClient, judge0Client, attemptService);
  }

  @Test
  void shouldReturnCorrectWhenAllHiddenTestsAreAccepted() {
    Exercise exercise = exercise();
    User user = user();
    ExerciseContentDefinition contentDefinition =
        contentDefinition(List.of(new ExerciseContentDefinition.TestCase("", "25\n")));

    when(exerciseRepository.findById(EXERCISE_ID)).thenReturn(Optional.of(exercise));
    when(gitHubExerciseContentClient.getExerciseContent("java-variables-001"))
        .thenReturn(contentDefinition);
    when(judge0Client.createSubmission(SOURCE_CODE, "", "25\n", 2, 128000))
        .thenReturn("submission-token");
    when(judge0Client.awaitSubmissionStatus("submission-token"))
        .thenReturn(new Judge0SubmissionStatus(3));

    ExerciseSubmissionResult result = service.submit(EXERCISE_ID, SOURCE_CODE, user);

    assertThat(result.correct()).isTrue();

    verify(judge0Client).createSubmission(SOURCE_CODE, "", "25\n", 2, 128000);
    verify(judge0Client).awaitSubmissionStatus("submission-token");
    verify(attemptService).recordAttempt(user, exercise, true);
  }

  @Test
  void shouldReturnIncorrectAndStopAfterFirstFailedHiddenTest() {
    Exercise exercise = exercise();
    User user = user();
    ExerciseContentDefinition contentDefinition =
        contentDefinition(
            List.of(
                new ExerciseContentDefinition.TestCase("", "25\n"),
                new ExerciseContentDefinition.TestCase("", "26\n")));

    when(exerciseRepository.findById(EXERCISE_ID)).thenReturn(Optional.of(exercise));
    when(gitHubExerciseContentClient.getExerciseContent("java-variables-001"))
        .thenReturn(contentDefinition);
    when(judge0Client.createSubmission(SOURCE_CODE, "", "25\n", 2, 128000))
        .thenReturn("failed-submission-token");
    when(judge0Client.awaitSubmissionStatus("failed-submission-token"))
        .thenReturn(new Judge0SubmissionStatus(4));

    ExerciseSubmissionResult result = service.submit(EXERCISE_ID, SOURCE_CODE, user);

    assertThat(result.correct()).isFalse();

    verify(judge0Client).createSubmission(SOURCE_CODE, "", "25\n", 2, 128000);
    verify(judge0Client).awaitSubmissionStatus("failed-submission-token");
    verifyNoMoreInteractions(judge0Client);
    verify(attemptService).recordAttempt(user, exercise, false);
  }

  // attempt
  private User user() {
    return new User("test-user", "test-user@example.com", "not-a-real-password-hash");
  }

  private Exercise exercise() {
    return new Exercise(
        "java-variables-001",
        "Print an Age Variable",
        "Declare an int variable named age and print it.",
        "Beginner",
        "CodeCalibrate");
  }

  private ExerciseContentDefinition contentDefinition(
      List<ExerciseContentDefinition.TestCase> tests) {
    return new ExerciseContentDefinition(
        "java-variables-001",
        1,
        "java",
        "Print an Age Variable",
        "Declare an int variable named age and print it.",
        "Beginner",
        "public class Main {}",
        List.of(),
        new ExerciseContentDefinition.Execution("Main", 2, 128000, tests));
  }
}
