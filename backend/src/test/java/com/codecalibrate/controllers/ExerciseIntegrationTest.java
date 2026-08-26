package com.codecalibrate.controllers;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.SkillRepository;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SkillRepository skillRepository;

    private Exercise exercise;
    private String skillName;

    @MockitoBean
    private GitHubExerciseContentClient gitHubExerciseContentClient;

    @BeforeEach
    public void setUp() {
        String testId = UUID.randomUUID().toString();
        skillName = "Test Variables " + testId;

        Skill variables = skillRepository.save(new Skill(
                skillName,
                "Temporary skill used only by this integration test.",
                "Beginner"
        ));

        exercise = new Exercise(
                "test-exercise-" + testId,
                "Print an Age Variable",
                "Declare an int variable named age, assign it the value 25, and print it.",
                null,
                "Beginner",
                "CodeCalibrate"
        );

        exercise.addSkill(variables);
        exerciseRepository.saveAndFlush(exercise);

        when(gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId()))
                .thenReturn(new ExerciseContentDefinition(
                        exercise.getExternalId(),
                        1,
                        "java",
                        "Print an Age Variable",
                        "Write a Java program that declares an int variable named age, assigns it the value 25, and prints it.",
                        "Beginner",

                        """
                                public class Main {
                                    public static void main(String[] args) {
                                    // Write your code here...
                                    }
                                    }
                                """,
                        null
                ));
    }

    @Test
    void shouldReturnPublicExerciseWithoutExpectedAnswer() throws Exception {
        mockMvc.perform(get("/api/exercises/{id}", exercise.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(exercise.getId()))
                .andExpect(jsonPath("$.externalId").value(exercise.getExternalId()))
                .andExpect(jsonPath("$.title").value("Print an Age Variable"))
                .andExpect(jsonPath("$.difficulty").value("Beginner"))
                .andExpect(jsonPath("$.skills[0].name").value(skillName))
                .andExpect(jsonPath("$.expectedAnswer").doesNotExist())
                .andExpect(jsonPath("$.starterCode")
                        .value(containsString("public class Main")))
                .andExpect(jsonPath("$.execution").doesNotExist())
                .andExpect(jsonPath("$.tests").doesNotExist());
    }

    @Test
    void shouldReturnNotFoundForUnknownExercise() throws Exception {
        mockMvc.perform(get("/api/exercises/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Exercise with ID 999 was not found."));
    }
}