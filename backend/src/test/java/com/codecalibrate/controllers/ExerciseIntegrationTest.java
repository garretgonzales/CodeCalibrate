package com.codecalibrate.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.SkillRepository;
import com.codecalibrate.data.UserRepository;
import com.codecalibrate.domain.JwtService;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.domain.judge.Judge0Client;
import com.codecalibrate.domain.judge.Judge0SubmissionStatus;
import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ExerciseRepository exerciseRepository;

  @Autowired private SkillRepository skillRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private JwtService jwtService;

  @MockitoBean private GitHubExerciseContentClient gitHubExerciseContentClient;

  @MockitoBean private Judge0Client judge0Client;

  @Autowired private AttemptRepository attemptRepository;

  @Autowired private EntityManager entityManager;

  private Exercise exercise;
  private String skillName;
  private String token;
  private User user;

  @BeforeEach
  public void setUp() {
    String testId = UUID.randomUUID().toString();
    skillName = "Test Variables " + testId;

    Skill variables =
        skillRepository.save(
            new Skill(
                skillName, "Temporary skill used only by this integration test.", "Beginner"));

    exercise =
        new Exercise(
            "test-exercise-" + testId,
            "Print an Age Variable",
            "Declare an int variable named age, assign it the value 25, and print it.",
            null,
            "Beginner",
            "CodeCalibrate");

    exercise.addSkill(variables);
    exerciseRepository.saveAndFlush(exercise);

    user =
        userRepository.save(
            new User(
                "user" + testId.replace("-", ""),
                "user-" + testId + "@example.com",
                "not-being-used-in-test"));

    token = jwtService.generateToken(user);

    when(gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId()))
        .thenReturn(publicContentDefinition());
  }

  private ExerciseContentDefinition publicContentDefinition() {
    return new ExerciseContentDefinition(
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
        null);
  }

  @Test
  void shouldReturnPublicExerciseWithoutExpectedAnswer() throws Exception {
    mockMvc
        .perform(get("/api/exercises/{id}", exercise.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(exercise.getId()))
        .andExpect(jsonPath("$.externalId").value(exercise.getExternalId()))
        .andExpect(jsonPath("$.title").value("Print an Age Variable"))
        .andExpect(jsonPath("$.difficulty").value("Beginner"))
        .andExpect(jsonPath("$.skills[0].name").value(skillName))
        .andExpect(jsonPath("$.expectedAnswer").doesNotExist())
        .andExpect(jsonPath("$.starterCode").value(containsString("public class Main")))
        .andExpect(jsonPath("$.execution").doesNotExist())
        .andExpect(jsonPath("$.tests").doesNotExist());
  }

  @Test
  void shouldReturnNotFoundForUnknownExercise() throws Exception {
    mockMvc
        .perform(get("/api/exercises/{id}", 999))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Exercise with ID 999 was not found."));
  }

  @Test
  void shouldReturnCorrectVerdictForAuthenticatedSubmission() throws Exception {
    when(gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId()))
        .thenReturn(executableContentDefinition());

    when(judge0Client.createSubmission("public class Main {}", "", "25\n", 2, 128000))
        .thenReturn("submission-token");

    when(judge0Client.awaitSubmissionStatus("submission-token"))
        .thenReturn(new Judge0SubmissionStatus(3));

    mockMvc
        .perform(
            post("/api/exercises/{id}/submissions", exercise.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                "sourceCode": "public class Main {}"
                                }
                                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.correct").value(true))
        .andExpect(jsonPath("$.sourceCode").doesNotExist())
        .andExpect(jsonPath("$.token").doesNotExist());

    entityManager.flush();
    entityManager.clear();

    Attempt attempt = attemptRepository.findAll().stream().findFirst().orElseThrow();

    assertThat(attempt.getUser().getId()).isEqualTo(user.getId());
    assertThat(attempt.getExercise().getId()).isEqualTo(exercise.getId());
    assertThat(attempt.isCorrect()).isTrue();
    assertThat(attempt.getAttemptedAt()).isNotNull();
  }

  @Test
  void shouldRejectSubmissionWithoutJwt() throws Exception {
    mockMvc
        .perform(
            post("/api/exercises/{id}/submissions", exercise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "sourceCode": "public class Main {}"
                                }
                                """))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldRejectBlankSourceCodeBeforeJudgeExecution() throws Exception {
    mockMvc
        .perform(
            post("/api/exercises/{id}/submissions", exercise.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "sourceCode": " "
                                }
                                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Request validation failed."))
        .andExpect(jsonPath("$.errors.sourceCode").value("Source code is required."));
  }

  private ExerciseContentDefinition executableContentDefinition() {
    return new ExerciseContentDefinition(
        exercise.getExternalId(),
        1,
        "java",
        "Print an Age Variable",
        "Write a Java program that declares an int variable named age, assigns it the value 25, and prints it.",
        "Beginner",
        "public class Main {}",
        new ExerciseContentDefinition.Execution(
            "Main", 2, 128000, List.of(new ExerciseContentDefinition.TestCase("", "25\n"))));
  }
}