package com.codecalibrate.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserMasteryPersistenceTest {

  @Autowired private UserMasteryRepository userMasteryRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private SkillRepository skillRepository;

  @Autowired private EntityManager entityManager;

  private User user;
  private Skill skill;

  @BeforeEach
  void setUp() {
    String testId = UUID.randomUUID().toString();

    user =
        userRepository.save(
            new User(
                "mastery-user-" + testId,
                "mastery-user-" + testId + "@example.com",
                "not-a-real-password-hash"));

    skill =
        skillRepository.save(
            new Skill(
                "Mastery Test Skill " + testId,
                "Temporary skill used only by this persistence test.",
                "Beginner"));
  }

  @Test
  void shouldPersistUpdatedMasteryForUserAndSkill() {
    UserMastery mastery = new UserMastery(user, skill);

    mastery.recordAttempt(true);
    mastery.recordAttempt(false);

    userMasteryRepository.saveAndFlush(mastery);

    entityManager.clear();

    UserMastery foundMastery = userMasteryRepository.findByUserAndSkill(user, skill).orElseThrow();

    assertThat(foundMastery.getQuestionsAttempted()).isEqualTo(2);
    assertThat(foundMastery.getQuestionsCorrect()).isEqualTo(1);
    assertThat(foundMastery.getMasteryScore()).isEqualByComparingTo("50.00");
    assertThat(foundMastery.getLastPracticedAt()).isNotNull();
  }
}