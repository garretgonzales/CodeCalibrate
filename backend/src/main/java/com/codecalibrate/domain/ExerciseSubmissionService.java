package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.domain.judge.Judge0Client;
import com.codecalibrate.domain.judge.Judge0SubmissionStatus;
import com.codecalibrate.models.Exercise;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseSubmissionService {

    private final ExerciseRepository exerciseRepository;
    private final GitHubExerciseContentClient gitHubExerciseContentClient;
    private final Judge0Client judge0Client;

    public ExerciseSubmissionService(
            ExerciseRepository exerciseRepository,
            GitHubExerciseContentClient gitHubExerciseContentClient,
            Judge0Client judge0Client
    ) {
        this.exerciseRepository = exerciseRepository;
        this.gitHubExerciseContentClient = gitHubExerciseContentClient;
        this.judge0Client = judge0Client;
    }

    public ExerciseSubmissionResult submit(Integer exerciseId, String sourceCode) {
        if (sourceCode == null || sourceCode.isBlank()) {
            throw new IllegalArgumentException("Source code is required.");
        }

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

        ExerciseContentDefinition contentDefinition =
                gitHubExerciseContentClient.getExerciseContent(
                        exercise.getExternalId()
                );

        if (!exercise.getExternalId().equals(contentDefinition.id())) {
            throw new IllegalStateException(
                    "Exercise content does not match the requested exercise."
            );
        }

        if (!"java".equals(contentDefinition.language())) {
            throw new IllegalStateException("Exercise language is not supported.");
        }

        ExerciseContentDefinition.Execution execution =
                contentDefinition.execution();

        if (execution == null
                || execution.timeLimitSeconds() == null
                || execution.memoryLimitKilobytes() == null
                || execution.tests() == null
                || execution.tests().isEmpty()) {
            throw new IllegalStateException(
                    "Exercise execution configuration is invalid."
            );
        }

        return evaluateHiddenTests(sourceCode, execution);
    }

    private ExerciseSubmissionResult evaluateHiddenTests(
            String sourceCode,
            ExerciseContentDefinition.Execution execution
    ) {
        List<ExerciseContentDefinition.TestCase> tests = execution.tests();

        for (ExerciseContentDefinition.TestCase testCase : tests) {
            if (testCase.stdin() == null || testCase.expectedStdout() == null) {
                throw new IllegalStateException(
                        "Exercise test configuration is invalid."
                );
            }

            String token = judge0Client.createSubmission(
                    sourceCode,
                    testCase.stdin(),
                    testCase.expectedStdout(),
                    execution.timeLimitSeconds(),
                    execution.memoryLimitKilobytes()
            );

            Judge0SubmissionStatus status =
                    judge0Client.awaitSubmissionStatus(token);

            if (!status.isAccepted()) {
                return new ExerciseSubmissionResult(false);
            }
        }

        return new ExerciseSubmissionResult(true);
    }
}