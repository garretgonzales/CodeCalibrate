package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.dto.ExerciseResponse;
import com.codecalibrate.dto.SkillResponse;
import com.codecalibrate.models.Exercise;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final GitHubExerciseContentClient gitHubExerciseContentClient;

    public ExerciseService(ExerciseRepository exerciseRepository, GitHubExerciseContentClient gitHubExerciseContentClient) {
        this.exerciseRepository = exerciseRepository;
        this.gitHubExerciseContentClient = gitHubExerciseContentClient;
    }

    public ExerciseResponse getExerciseById(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));

        ExerciseContentDefinition contentDefinition = gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId());

        if (!exercise.getExternalId().equals(contentDefinition.id())) {
            throw new IllegalStateException("Exercise content does not match the requested exercise.");
        }

        return toResponse(exercise, contentDefinition);
    }

    private ExerciseResponse toResponse(Exercise exercise, ExerciseContentDefinition contentDefinition) {
        List<SkillResponse> skills = exercise.getSkills().stream()
                .map(skill -> new SkillResponse(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getDifficulty()
                ))
                .toList();

        return new ExerciseResponse(
                exercise.getId(),
                exercise.getExternalId(),
                exercise.getTitle(),
                exercise.getDescription(),
                exercise.getDifficulty(),
                exercise.getSource(),
                contentDefinition.starterCode(),
                skills
        );
    }
}