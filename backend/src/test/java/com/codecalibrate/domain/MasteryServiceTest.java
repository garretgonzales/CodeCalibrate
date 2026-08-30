package com.codecalibrate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codecalibrate.data.ExerciseRepository;
import com.codecalibrate.data.UserMasteryRepository;
import com.codecalibrate.data.UserRepository;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class MasteryServiceTest {

  private static final Integer USER_ID = 1;
  private static final Integer EXERCISE_ID = 1;

  private UserMasteryRepository userMasteryRepository;
  private UserRepository userRepository;
  private ExerciseRepository exerciseRepository;
  private MasteryService service;

  @BeforeEach
  void setUp() {
    userMasteryRepository = mock(UserMasteryRepository.class);
    userRepository = mock(UserRepository.class);
    exerciseRepository = mock(ExerciseRepository.class);

    service = new MasteryService(userMasteryRepository, userRepository, exerciseRepository);
  }

  @Test
  void shouldCreateMasteryForEachExerciseSkill() {
    User user = new User("mastery-user", "mastery-user@example.com", "not-a-real-password-hash");

    Skill variables = new Skill("Variables", "Store and use values.", "Beginner");

    Skill output = new Skill("Output", "Print values to the console.", "Beginner");

    Exercise exercise =
        new Exercise(
            "java-variables-001",
            "Print an Age Variable",
            "Temporary exercise for this test.",
            "Beginner",
            "CodeCalibrate");

    exercise.addSkill(variables);
    exercise.addSkill(output);

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(exerciseRepository.findById(EXERCISE_ID)).thenReturn(Optional.of(exercise));
    when(userMasteryRepository.findByUserAndSkill(user, variables)).thenReturn(Optional.empty());
    when(userMasteryRepository.findByUserAndSkill(user, output)).thenReturn(Optional.empty());

    service.recordAttempt(USER_ID, EXERCISE_ID, true);

    ArgumentCaptor<UserMastery> masteryCaptor = ArgumentCaptor.forClass(UserMastery.class);

    verify(userMasteryRepository, times(2)).save(masteryCaptor.capture());

    List<UserMastery> savedMasteries = masteryCaptor.getAllValues();

    assertThat(savedMasteries)
        .allSatisfy(
            mastery -> {
              assertThat(mastery.getUser()).isSameAs(user);
              assertThat(mastery.getQuestionsAttempted()).isEqualTo(1);
              assertThat(mastery.getQuestionsCorrect()).isEqualTo(1);
              assertThat(mastery.getMasteryScore()).isEqualByComparingTo("100.00");
            });

    assertThat(savedMasteries)
        .extracting(UserMastery::getSkill)
        .containsExactlyInAnyOrder(variables, output);
  }
}
