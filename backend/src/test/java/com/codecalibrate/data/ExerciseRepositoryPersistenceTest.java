package com.codecalibrate.data;

import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ExerciseRepositoryPersistenceTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EntityManager entityManager;

    private Exercise variablesExercise;
    private String skillName;

    @BeforeEach
    public void setup() {
        String testId = UUID.randomUUID().toString();
        skillName = "Test Variables " + testId;

        Skill variables = skillRepository.save(
                new Skill(
                        skillName,
                        "Temporary skill used only by this persistence test.",
                        "Beginner"
                )
        );

        variablesExercise = new Exercise(
                "test-exercise-" + testId,
                "Print an Age Variable",
                "Declare an int variable named age, assign it the value 25, and print it.",
                null,
                "Beginner",
                "CodeCalibrate"
        );

        variablesExercise.addSkill(variables);
        exerciseRepository.saveAndFlush(variablesExercise);

        // Forces assertions to reload the entity and relationship from H2.
        entityManager.clear();
    }

    @Test
    void shouldPersistExerciseWithItsSkill() {
        Exercise savedExercise = exerciseRepository.findById(variablesExercise.getId())
                .orElseThrow();

        assertThat(savedExercise.getTitle()).isEqualTo("Print an Age Variable");
        assertThat(savedExercise.getSkills())
                .extracting(Skill::getName)
                .containsExactly(skillName);
    }
}