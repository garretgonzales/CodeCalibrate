package com.codecalibrate.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codecalibrate.data.*;
import com.codecalibrate.domain.Judge0UnavailableException;
import com.codecalibrate.domain.JwtService;
import com.codecalibrate.domain.RecommendationService;
import com.codecalibrate.domain.content.ExerciseContentDefinition;
import com.codecalibrate.domain.content.GitHubExerciseContentClient;
import com.codecalibrate.domain.judge.Judge0Client;
import com.codecalibrate.domain.judge.Judge0SubmissionStatus;
import com.codecalibrate.models.*;
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

  @MockitoBean private RecommendationService recommendationService;

  @Autowired private ExerciseRepository exerciseRepository;

  @Autowired private SkillRepository skillRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private JwtService jwtService;

  @MockitoBean private GitHubExerciseContentClient gitHubExerciseContentClient;

  @MockitoBean private Judge0Client judge0Client;

  @Autowired private AttemptRepository attemptRepository;

  @Autowired private UserMasteryRepository userMasteryRepository;

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
    return publicContentDefinition(
        List.of(
            new ExerciseContentDefinition.DocumentationReference(
                "Java variables",
                "Review Java variable types, naming, and initialization.",
                "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html"),
            new ExerciseContentDefinition.DocumentationReference(
                "Defining methods in Java",
                "Review how Java methods declare parameters and return values.",
                "https://dev.java/learn/classes-objects/defining-methods/")));
  }

  private ExerciseContentDefinition publicContentDefinition(
      List<ExerciseContentDefinition.DocumentationReference> references) {
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
        references,
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
        .andExpect(jsonPath("$.references[0].label").value("Java variables"))
        .andExpect(
            jsonPath("$.references[0].description")
                .value("Review Java variable types, naming, and initialization."))
        .andExpect(
            jsonPath("$.references[0].url")
                .value("https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html"))
        .andExpect(jsonPath("$.references[1].label").value("Defining methods in Java"))
        .andExpect(
            jsonPath("$.references[1].description")
                .value("Review how Java methods declare parameters and return values."))
        .andExpect(
            jsonPath("$.references[1].url")
                .value("https://dev.java/learn/classes-objects/defining-methods/"))
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
  void shouldReturnRecommendedExerciseForAuthenticatedUser() throws Exception {
    when(recommendationService.recommendNextExercise(any(User.class))).thenReturn(exercise);

    mockMvc
        .perform(
            get("/api/exercises/recommended").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(exercise.getId()))
        .andExpect(jsonPath("$.title").value("Print an Age Variable"))
        .andExpect(jsonPath("$.starterCode").value(containsString("public class Main")))
        .andExpect(jsonPath("$.expectedAnswer").doesNotExist())
        .andExpect(jsonPath("$.execution").doesNotExist())
        .andExpect(jsonPath("$.tests").doesNotExist());
  }

  @Test
  void shouldRejectRecommendedExerciseRequestWithoutJwt() throws Exception {
    mockMvc.perform(get("/api/exercises/recommended")).andExpect(status().isUnauthorized());
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

    UserMastery mastery = userMasteryRepository.findAll().stream().findFirst().orElseThrow();

    assertThat(mastery.getUser().getId()).isEqualTo(user.getId());
    assertThat(mastery.getSkill().getName()).isEqualTo(skillName);
    assertThat(mastery.getQuestionsAttempted()).isEqualTo(1);
    assertThat(mastery.getQuestionsCorrect()).isEqualTo(1);
    assertThat(mastery.getMasteryScore()).isEqualByComparingTo("100.00");
    assertThat(mastery.getLastPracticedAt()).isNotNull();
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
        List.of(),
        new ExerciseContentDefinition.Execution(
            "Main", 2, 128000, List.of(new ExerciseContentDefinition.TestCase("", "25\n"))));
  }

  @Test
  void shouldReturnServiceUnavailableWhenJudge0IsUnavailable() throws Exception {
    when(gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId()))
        .thenReturn(executableContentDefinition());

    when(judge0Client.createSubmission("public class Main {}", "", "25\n", 2, 128000))
        .thenThrow(
            new Judge0UnavailableException(new RuntimeException("private provider details")));

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
        .andExpect(status().isServiceUnavailable())
        .andExpect(
            jsonPath("$.message")
                .value("Exercise validation is temporarily unavailable. Please try again."))
        .andExpect(
            jsonPath("$.message")
                .value(org.hamcrest.Matchers.not(containsString("private provider details"))))
        .andExpect(jsonPath("$.errors").isEmpty());

    assertThat(attemptRepository.count()).isZero();
    assertThat(userMasteryRepository.count()).isZero();
  }

  @Test
  void shouldRejectUnapprovedDocumentationReference() {
    when(gitHubExerciseContentClient.getExerciseContent(exercise.getExternalId()))
        .thenReturn(
            publicContentDefinition(
                List.of(
                    new ExerciseContentDefinition.DocumentationReference(
                        "Unapproved reference",
                        "This host is not approved.",
                        "https://example.com/java"))));

    assertThatThrownBy(() -> mockMvc.perform(get("/api/exercises/{id}", exercise.getId())))
        .hasRootCauseInstanceOf(IllegalStateException.class)
        .hasRootCauseMessage("Exercise documentation reference is not approved.");
  }
}
