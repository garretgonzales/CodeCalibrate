package com.codecalibrate.data;

import com.codecalibrate.models.LearningPath;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningPathRepository extends JpaRepository<LearningPath, Integer> {

    @EntityGraph(attributePaths = "skills")
    List<LearningPath> findAll();

    @Override
    @EntityGraph(attributePaths = "skills")
    Optional<LearningPath> findById(Integer id);

}
