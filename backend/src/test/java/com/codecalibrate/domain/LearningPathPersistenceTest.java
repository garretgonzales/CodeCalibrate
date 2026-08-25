package com.codecalibrate.domain;


import com.codecalibrate.data.LearningPathRepository;
import com.codecalibrate.data.SkillRepository;
import com.codecalibrate.models.LearningPath;
import com.codecalibrate.models.Skill;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LearningPathPersistenceTest {

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EntityManager entityManager;

    // not a bean
    private LearningPath javaPath;

    @BeforeEach
    public void setup() {
        learningPathRepository.deleteAll();
        skillRepository.deleteAll();
        entityManager.flush();

        Skill variables = skillRepository.save(new Skill("Variables", "Store and work with values in Java.", "Beginner"));

        Skill controlFlow = skillRepository.save(new Skill("Control Flow", "Use conditionals and loops to control program behavior.", "Beginner"));

        Skill methods = skillRepository.save(new Skill("Methods", "Organize reusable behavior into methods.", "Beginner"));

        javaPath = new LearningPath("Java", "Build foundational Java programming skills.", "Java");

        javaPath.addSkill(variables);
        javaPath.addSkill(controlFlow);
        javaPath.addSkill(methods);

        learningPathRepository.saveAndFlush(javaPath);
        /* must force the assertion to read the relationship from h2
         proves h2 can reload ordered relationship from the db*/
        entityManager.clear();

    }

    @Test
    void shouldPersistJavaPathWithSkillsInTeachingOrder() {
        LearningPath savedPath = learningPathRepository.findById(javaPath.getId())
                .orElseThrow();

        assertThat(savedPath.getName()).isEqualTo("Java");
        assertThat(savedPath.getLanguage()).isEqualTo("Java");
        assertThat(savedPath.getSkills())
                .extracting(Skill::getName)
                .containsExactly(
                        "Variables",
                        "Control Flow",
                        "Methods"
                );
    }

}
