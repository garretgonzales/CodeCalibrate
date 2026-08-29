package com.codecalibrate.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(
    name = "user_mastery",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "skill_id"}))
public class UserMastery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "skill_id", nullable = false)
  private Skill skill;

  @Column(name = "mastery_score", nullable = false, precision = 5, scale = 2)
  private BigDecimal masteryScore = BigDecimal.ZERO.setScale(2);

  @Column(name = "questions_attempted", nullable = false)
  private int questionsAttempted;

  @Column(name = "questions_correct", nullable = false)
  private int questionsCorrect;

  @Column(name = "last_practiced_at")
  private LocalDateTime lastPracticedAt;

  protected UserMastery() {}

  public UserMastery(User user, Skill skill) {
    this.user = user;
    this.skill = skill;
  }

  public void recordAttempt(boolean correct) {
    questionsAttempted++;

    if (correct) {
      questionsCorrect++;
    }

    masteryScore =
        BigDecimal.valueOf(questionsCorrect)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(questionsAttempted), 2, RoundingMode.HALF_UP);

    lastPracticedAt = LocalDateTime.now(ZoneOffset.UTC);
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Skill getSkill() {
    return skill;
  }

  public BigDecimal getMasteryScore() {
    return masteryScore;
  }

  public int getQuestionsAttempted() {
    return questionsAttempted;
  }

  public int getQuestionsCorrect() {
    return questionsCorrect;
  }

  public LocalDateTime getLastPracticedAt() {
    return lastPracticedAt;
  }
}