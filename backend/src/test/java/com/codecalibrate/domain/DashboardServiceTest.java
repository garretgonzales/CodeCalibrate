package com.codecalibrate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.LearningPathRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.dto.DashboardResponse;
import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.LearningPath;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DashboardServiceTest {

    private AttemptRepository attemptRepository;
    private ExerciseRepository exerciseRepository;
    private LearningPathRepository learningPathRepository;
    private UserMasteryRepository userMasteryRepository;
    private RecommendationService recommendationService;
    private DashboardService service;

    @BeforeEach
    void setUp() {
        attemptRepository = mock(AttemptRepository.class);
        exerciseRepository = mock(ExerciseRepository.class);
        learningPathRepository = mock(LearningPathRepository.class);
        userMasteryRepository = mock(UserMasteryRepository.class);
        recommendationService = mock(RecommendationService.class);

        service =
                new DashboardService(
                        attemptRepository,
                        exerciseRepository,
                        learningPathRepository,
                        userMasteryRepository,
                        recommendationService);
    }

    @Test
    void shouldBuildEmptyDashboardForNewUser() {
        User user = mock(User.class);
        Exercise recommendedExercise = mock(Exercise.class);
        LocalDateTime memberSince =
                LocalDateTime.of(2026, 9, 4, 12, 30);

        when(user.getUsername()).thenReturn("new-learner");
        when(user.getEmail()).thenReturn("new-learner@example.com");
        when(user.getCreatedAt()).thenReturn(memberSince);

        when(userMasteryRepository
                .findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user))
                .thenReturn(List.of());
        when(attemptRepository.findDistinctCorrectExerciseIdsByUser(user))
                .thenReturn(List.of());
        when(attemptRepository.findTop5ByUserOrderByAttemptedAtDesc(user))
                .thenReturn(List.of());
        when(learningPathRepository.findAll()).thenReturn(List.of());

        when(recommendedExercise.getId()).thenReturn(1);
        when(recommendedExercise.getTitle())
                .thenReturn("Print an Age Variable");
        when(recommendedExercise.getDescription())
                .thenReturn("Declare and print an integer variable.");
        when(recommendedExercise.getDifficulty()).thenReturn("Beginner");
        when(recommendedExercise.getSkills()).thenReturn(List.of());
        when(recommendationService.recommendNextExercise(user))
                .thenReturn(recommendedExercise);

        DashboardResponse result = service.getDashboard(user);

        assertThat(result.user().username()).isEqualTo("new-learner");
        assertThat(result.user().email())
                .isEqualTo("new-learner@example.com");
        assertThat(result.user().memberSince())
                .isEqualTo(memberSince.toInstant(ZoneOffset.UTC));

        assertThat(result.overview().totalAttempts()).isZero();
        assertThat(result.overview().correctAttempts()).isZero();
        assertThat(result.overview().completedExercises()).isZero();
        assertThat(result.overview().accuracy())
                .isEqualByComparingTo("0.00");
        assertThat(result.overview().averageMastery())
                .isEqualByComparingTo("0.00");

        assertThat(result.recommendedExercise().id()).isEqualTo(1);
        assertThat(result.skillMastery()).isEmpty();
        assertThat(result.recentAttempts()).isEmpty();
        assertThat(result.pathProgress()).isEmpty();
    }

    @Test
    void shouldBuildDashboardForLearnerWithProgress() {
        User user = mock(User.class);

        Skill variables = skill(1, "Variables");
        Skill methods = skill(2, "Methods");

        LocalDateTime variablesPracticedAt =
                LocalDateTime.of(2026, 9, 3, 10, 0);
        LocalDateTime methodsPracticedAt =
                LocalDateTime.of(2026, 9, 4, 11, 0);

        UserMastery variablesMastery =
                mastery(
                        variables,
                        "50.00",
                        2,
                        1,
                        variablesPracticedAt);

        UserMastery methodsMastery =
                mastery(
                        methods,
                        "100.00",
                        1,
                        1,
                        methodsPracticedAt);

        Exercise sharedExercise =
                exercise(10, "Shared Exercise", variables, methods);
        Exercise methodsExercise =
                exercise(11, "Methods Exercise", methods);

        Attempt recentAttempt = mock(Attempt.class);
        when(recentAttempt.getExercise()).thenReturn(sharedExercise);
        when(recentAttempt.isCorrect()).thenReturn(true);
        when(recentAttempt.getAttemptedAt())
                .thenReturn(LocalDateTime.of(2026, 9, 4, 12, 0));

        LearningPath javaPath = mock(LearningPath.class);
        when(javaPath.getId()).thenReturn(1);
        when(javaPath.getName()).thenReturn("Java");
        when(javaPath.getLanguage()).thenReturn("Java");
        when(javaPath.getSkills())
                .thenReturn(List.of(variables, methods));

        when(user.getUsername()).thenReturn("progress-learner");
        when(user.getEmail()).thenReturn("progress@example.com");

        when(userMasteryRepository
                .findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user))
                .thenReturn(List.of(variablesMastery, methodsMastery));
        when(attemptRepository.findDistinctCorrectExerciseIdsByUser(user))
                .thenReturn(List.of(10));
        when(attemptRepository.findTop5ByUserOrderByAttemptedAtDesc(user))
                .thenReturn(List.of(recentAttempt));
        when(attemptRepository.countByUser(user)).thenReturn(3L);
        when(attemptRepository.countByUserAndCorrectTrue(user))
                .thenReturn(2L);
        when(learningPathRepository.findAll())
                .thenReturn(List.of(javaPath));
        when(exerciseRepository.findBySkillsContainingOrderByIdAsc(variables))
                .thenReturn(List.of(sharedExercise));
        when(exerciseRepository.findBySkillsContainingOrderByIdAsc(methods))
                .thenReturn(List.of(sharedExercise, methodsExercise));
        when(recommendationService.recommendNextExercise(user))
                .thenReturn(methodsExercise);

        DashboardResponse result = service.getDashboard(user);

        assertThat(result.overview().totalAttempts()).isEqualTo(3);
        assertThat(result.overview().correctAttempts()).isEqualTo(2);
        assertThat(result.overview().completedExercises()).isEqualTo(1);
        assertThat(result.overview().accuracy())
                .isEqualByComparingTo("66.67");
        assertThat(result.overview().averageMastery())
                .isEqualByComparingTo("75.00");

        assertThat(result.skillMastery()).hasSize(2);
        assertThat(result.skillMastery().get(0).name())
                .isEqualTo("Variables");
        assertThat(result.skillMastery().get(0).masteryScore())
                .isEqualByComparingTo("50.00");
        assertThat(result.skillMastery().get(1).name())
                .isEqualTo("Methods");

        assertThat(result.recentAttempts()).hasSize(1);
        assertThat(result.recentAttempts().getFirst().exerciseId())
                .isEqualTo(10);
        assertThat(result.recentAttempts().getFirst().correct()).isTrue();

        assertThat(result.pathProgress()).hasSize(1);
        assertThat(result.pathProgress().getFirst().totalExercises())
                .isEqualTo(2);
        assertThat(result.pathProgress().getFirst().completedExercises())
                .isEqualTo(1);

        assertThat(result.pathProgress().getFirst().skills().get(0)
                         .totalExercises()).isEqualTo(1);
        assertThat(result.pathProgress().getFirst().skills().get(0)
                         .completedExercises()).isEqualTo(1);
        assertThat(result.pathProgress().getFirst().skills().get(1)
                         .totalExercises()).isEqualTo(2);
        assertThat(result.pathProgress().getFirst().skills().get(1)
                         .completedExercises()).isEqualTo(1);

        assertThat(result.recommendedExercise().id()).isEqualTo(11);
        assertThat(result.recommendedExercise().skills())
                .containsExactly("Methods");

        verify(exerciseRepository, times(1))
                .findBySkillsContainingOrderByIdAsc(variables);
        verify(exerciseRepository, times(1))
                .findBySkillsContainingOrderByIdAsc(methods);
    }

    private Skill skill(Integer id, String name) {
        Skill skill = mock(Skill.class);
        when(skill.getId()).thenReturn(id);
        when(skill.getName()).thenReturn(name);
        when(skill.getDescription())
                .thenReturn("Temporary dashboard test skill.");
        when(skill.getDifficulty()).thenReturn("Beginner");
        return skill;
    }

    private UserMastery mastery(
            Skill skill,
            String score,
            int attempted,
            int correct,
            LocalDateTime lastPracticedAt) {
        UserMastery mastery = mock(UserMastery.class);
        when(mastery.getSkill()).thenReturn(skill);
        when(mastery.getMasteryScore())
                .thenReturn(new BigDecimal(score));
        when(mastery.getQuestionsAttempted()).thenReturn(attempted);
        when(mastery.getQuestionsCorrect()).thenReturn(correct);
        when(mastery.getLastPracticedAt()).thenReturn(lastPracticedAt);
        return mastery;
    }

    private Exercise exercise(
            Integer id,
            String title,
            Skill... skills) {
        Exercise exercise = mock(Exercise.class);
        when(exercise.getId()).thenReturn(id);
        when(exercise.getTitle()).thenReturn(title);
        when(exercise.getDescription())
                .thenReturn("Temporary dashboard test exercise.");
        when(exercise.getDifficulty()).thenReturn("Beginner");
        when(exercise.getSkills()).thenReturn(List.of(skills));
        return exercise;
    }
}
