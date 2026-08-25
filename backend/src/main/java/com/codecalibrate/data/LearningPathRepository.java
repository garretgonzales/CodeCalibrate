package com.codecalibrate.data;

import com.codecalibrate.models.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {
}
