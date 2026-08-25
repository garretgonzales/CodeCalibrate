package com.codecalibrate.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "learning_paths")
public class LearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 64)
    private String language;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "learning_path_skills",
            joinColumns = @JoinColumn(name = "learning_path_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )

    @OrderColumn(name = "sequence_order")
    private List<Skill> skills = new ArrayList<>();

    protected LearningPath() {}

    public LearningPath(String name, String description, String language) {
        this.name = name;
        this.description = description;
        this.language = language;
    }



    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }


    public Integer getId() {
        return id;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
