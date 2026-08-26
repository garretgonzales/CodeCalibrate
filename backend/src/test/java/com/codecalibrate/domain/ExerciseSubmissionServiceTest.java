package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.domain.judge.Judge0Client;
import com.codecalibrate.domain.judge.Judge0SubmissionStatus;
import com.codecalibrate.models.Exercise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ExerciseSubmissionServiceTest {

    private static final Integer EXERCISE_ID = 1;
    private static final String SOURCE_CODE = "public class Main {}";

    private ExerciseRepository exerciseRepository;
    private GitHubExerciseContentClient gitHubExerciseContentClient;
    private Judge0Client judge0Client;
    private ExerciseSubmissionService service;

    @BeforeEach
    void setUp() {
        exerciseRepository = mock(ExerciseRepository.class);
        gitHubExerciseContentClient = mock(GitHubExerciseContentClient.class);
        judge0Client = mock(Judge0Client.class);

        service = new ExerciseSubmissionService(
                exerciseRepository,
                gitHubExerciseContentClient,
                judge0Client
        );
    }

    @Test
    void shouldReturnCorrectWhenAllHiddenTestsAreAccepted() {
        Exercise exercise = exercise();
        ExerciseContentDefinition contentDefinition = contentDefinition(
                List.of(new ExerciseContentDefinition.TestCase("", "25\n"))
        );

        when(exerciseRepository.findById(EXERCISE_ID))
                .thenReturn(Optional.of(exercise));
        when(gitHubExerciseContentClient.getExerciseContent("java-variables-001"))
                .thenReturn(contentDefinition);
        when(judge0Client.createSubmission(
                SOURCE_CODE,
                "",
                "25\n",
                2,
                128000
        )).thenReturn("submission-token");
        when(judge0Client.awaitSubmissionStatus("submission-token"))
                .thenReturn(new Judge0SubmissionStatus(3));

        ExerciseSubmissionResult result =
                service.submit(EXERCISE_ID, SOURCE_CODE);

        assertThat(result.correct()).isTrue();

        verify(judge0Client).createSubmission(
                SOURCE_CODE,
                "",
                "25\n",
                2,
                128000
        );
        verify(judge0Client).awaitSubmissionStatus("submission-token");
    }

    @Test
    void shouldReturnIncorrectAndStopAfterFirstFailedHiddenTest() {
        Exercise exercise = exercise();
        ExerciseContentDefinition contentDefinition = contentDefinition(
                List.of(
                        new ExerciseContentDefinition.TestCase("", "25\n"),
                        new ExerciseContentDefinition.TestCase("", "26\n")
                )
        );

        when(exerciseRepository.findById(EXERCISE_ID))
                .thenReturn(Optional.of(exercise));
        when(gitHubExerciseContentClient.getExerciseContent("java-variables-001"))
                .thenReturn(contentDefinition);
        when(judge0Client.createSubmission(
                SOURCE_CODE,
                "",
                "25\n",
                2,
                128000
        )).thenReturn("failed-submission-token");
        when(judge0Client.awaitSubmissionStatus("failed-submission-token"))
                .thenReturn(new Judge0SubmissionStatus(4));

        ExerciseSubmissionResult result =
                service.submit(EXERCISE_ID, SOURCE_CODE);

        assertThat(result.correct()).isFalse();

        verify(judge0Client).createSubmission(
                SOURCE_CODE,
                "",
                "25\n",
                2,
                128000
        );
        verify(judge0Client).awaitSubmissionStatus("failed-submission-token");
        verifyNoMoreInteractions(judge0Client);
    }

    private Exercise exercise() {
        return new Exercise(
                "java-variables-001",
                "Print an Age Variable",
                "Declare an int variable named age and print it.",
                null,
                "Beginner",
                "CodeCalibrate"
        );
    }

    private ExerciseContentDefinition contentDefinition(
            List<ExerciseContentDefinition.TestCase> tests
    ) {
        return new ExerciseContentDefinition(
                "java-variables-001",
                1,
                "java",
                "Print an Age Variable",
                "Declare an int variable named age and print it.",
                "Beginner",
                "public class Main {}",
                new ExerciseContentDefinition.Execution(
                        "Main",
                        2,
                        128000,
                        tests
                )
        );
    }
}