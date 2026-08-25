package com.codecalibrate.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_id", nullable = false, length = 64)
    private String externalId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "expected_answer", columnDefinition = "text")
    private String expectedAnswer;

    @Column(length = 32)
    private String difficulty;

    @Column(nullable = false, length = 32)
    private String source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exercise_skills",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    protected Exercise() {
    }

    public Exercise(
            String externalId,
            String title,
            String description,
            String expectedAnswer,
            String difficulty,
            String source
    ) {
        this.externalId = externalId;
        this.title = title;
        this.description = description;
        this.expectedAnswer = expectedAnswer;
        this.difficulty = difficulty;
        this.source = source;
    }

    @PrePersist
    private void setCreatedAt() {
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public Integer getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getSource() {
        return source;
    }

    public List<Skill> getSkills() {
        return skills;
    }
}