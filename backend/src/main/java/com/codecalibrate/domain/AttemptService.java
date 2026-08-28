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

  public AttemptService(AttemptRepository attemptRepository) {
    this.attemptRepository = attemptRepository;
  }

  @Transactional
  public void recordAttempt(User user, Exercise exercise, boolean correct) {
    Attempt attempt = new Attempt(user, exercise, correct);
    attemptRepository.save(attempt);
  }
}