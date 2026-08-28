package com.codecalibrate.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "attempts")
public class Attempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "exercise_id", nullable = false)
  private Exercise exercise;

  @Column(name = "is_correct", nullable = false)
  private boolean correct;

  @Column(name = "attempted_at", nullable = false, updatable = false)
  private LocalDateTime attemptedAt;

  protected Attempt() {}

  public Attempt(User user, Exercise exercise, boolean correct) {
    this.user = user;
    this.exercise = exercise;
    this.correct = correct;
  }

  @PrePersist
  private void setAttemptedAt() {
    this.attemptedAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Exercise getExercise() {
    return exercise;
  }

  public boolean isCorrect() {
    return correct;
  }

  public LocalDateTime getAttemptedAt() {
    return attemptedAt;
  }
}