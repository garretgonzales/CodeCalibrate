package com.codecalibrate.domain;

import com.codecalibrate.data.AttemptRepository;
import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AttemptService {

  private final AttemptRepository attemptRepository;
  private final MasteryService masteryService;

  public AttemptService(AttemptRepository attemptRepository, MasteryService masteryService) {
    this.attemptRepository = attemptRepository;
    this.masteryService = masteryService;
  }

  @Transactional
  public void recordAttempt(User user, Exercise exercise, boolean correct) {
    Attempt attempt = new Attempt(user, exercise, correct);
    attemptRepository.save(attempt);

    masteryService.recordAttempt(user.getId(), exercise.getId(), correct);
  }
}