package com.codecalibrate.domain;

import com.codecalibrate.data.ExerciseRepository;
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

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public ExerciseResponse getExerciseById(Integer id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));

        return toResponse(exercise);
    }

    private ExerciseResponse toResponse(Exercise exercise) {
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
                skills
        );
    }
}