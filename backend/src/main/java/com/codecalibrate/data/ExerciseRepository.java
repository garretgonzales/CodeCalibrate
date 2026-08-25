package com.codecalibrate.data;

import com.codecalibrate.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

    // need to confirm metadata to private exercise def
}
