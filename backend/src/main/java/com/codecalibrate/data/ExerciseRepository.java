package com.codecalibrate.data;

import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    // need to confirm metadata to private exercise def

    List<Exercise> findBySkillsContainingOrderByIdAsc(Skill skill);

    Optional<Exercise> findFirstByOrderByIdAsc();

}