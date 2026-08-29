package com.codecalibrate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecommendationServiceTest {

  private UserMasteryRepository userMasteryRepository;
  private ExerciseRepository exerciseRepository;
  private RecommendationService service;

  @BeforeEach
  void setUp() {
    userMasteryRepository = mock(UserMasteryRepository.class);
    exerciseRepository = mock(ExerciseRepository.class);

    service = new RecommendationService(userMasteryRepository, exerciseRepository);
  }

  @Test
  void shouldRecommendExerciseForLowestMasterySkill() {
    User user = user();
    Skill weakSkill = skill("Variables");
    Skill strongSkill = skill("Methods");

    UserMastery weakMastery = new UserMastery(user, weakSkill);
    weakMastery.recordAttempt(false);

    UserMastery strongMastery = new UserMastery(user, strongSkill);
    strongMastery.recordAttempt(true);

    Exercise recommendedExercise = mock(Exercise.class);

    when(userMasteryRepository.findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user))
        .thenReturn(List.of(weakMastery, strongMastery));
    when(exerciseRepository.findBySkillsContainingOrderByIdAsc(weakSkill))
        .thenReturn(List.of(recommendedExercise));

    Exercise result = service.recommendNextExercise(user);

    assertThat(result).isSameAs(recommendedExercise);

    verify(exerciseRepository).findBySkillsContainingOrderByIdAsc(weakSkill);
    verify(exerciseRepository, never()).findFirstByOrderByIdAsc();
  }

  @Test
  void shouldRecommendFirstExerciseForNewUser() {
    User user = user();
    Exercise firstExercise = mock(Exercise.class);

    when(userMasteryRepository.findByUserOrderByMasteryScoreAscLastPracticedAtAsc(user))
        .thenReturn(List.of());
    when(exerciseRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(firstExercise));

    Exercise result = service.recommendNextExercise(user);

    assertThat(result).isSameAs(firstExercise);

    verify(exerciseRepository).findFirstByOrderByIdAsc();
  }

  private User user() {
    return new User(
        "recommendation-user", "recommendation-user@example.com", "not-a-real-password-hash");
  }

  private Skill skill(String name) {
    return new Skill(name, "Temporary skill used only by this recommendation test.", "Beginner");
  }
}