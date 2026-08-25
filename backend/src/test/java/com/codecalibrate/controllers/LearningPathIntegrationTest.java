package com.codecalibrate.controllers;


import com.codecalibrate.data.LearningPathRepository;
import com.codecalibrate.data.SkillRepository;
import com.codecalibrate.models.LearningPath;
import com.codecalibrate.models.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LearningPathIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private SkillRepository skillRepository;

    private LearningPath javaPath;

    @BeforeEach
    public void setUp() {
        learningPathRepository.deleteAll();
        skillRepository.deleteAll();

        Skill variables = skillRepository.save(new Skill(
                "Variables",
                "Store and work with values in Java.",
                "Beginner"
        ));

        Skill controlFlow = skillRepository.save(new Skill(
                "Control Flow",
                "Store and work with values in Java.",
                "Beginner"
        ));

        Skill methods = skillRepository.save(new Skill(
                "Methods",
                "Organize reusable behavior into methods.",
                "Beginner"
        ));

        javaPath = new LearningPath(
                "Java",
                "Build foundational Java programming skills.",
                "Java"
        );

        javaPath.addSkill(variables);
        javaPath.addSkill(controlFlow);
        javaPath.addSkill(methods);

        learningPathRepository.saveAndFlush(javaPath);

    }

    @Test
    void shouldReturnPublicLearningPathsWithOrderedSkills() throws Exception {
        mockMvc.perform(get("/api/learning-paths"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[0].skills[0].name").value("Variables"))
                .andExpect(jsonPath("$[0].skills[1].name").value("Control Flow"))
                .andExpect(jsonPath("$[0].skills[2].name").value("Methods"));
    }

    @Test
    void shouldReturnLearningPathById() throws Exception {
        mockMvc.perform(get("/api/learning-paths/{id}", javaPath.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(javaPath.getId()))
                .andExpect(jsonPath("$.language").value("Java"));
    }

    @Test
    void shouldReturnNotFoundForUnknownLearningPath() throws Exception {
        mockMvc.perform(get("/api/learning-paths/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Learning path with ID 999 was not found."));
    }
}


