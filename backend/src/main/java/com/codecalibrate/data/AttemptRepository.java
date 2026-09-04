package com.codecalibrate.data;

import com.codecalibrate.models.Attempt;
import com.codecalibrate.models.Exercise;
import com.codecalibrate.models.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

  boolean existsByUserAndExercise(User user, Exercise exercise);

  long countByUser(User user);

  long countByUserAndCorrectTrue(User user);

  @Query(
      """
            select distinct attempt.exercise.id
            from Attempt attempt
            where attempt.user = :user
              and attempt.correct = true
            """)
  List<Integer> findDistinctCorrectExerciseIdsByUser(@Param("user") User user);

  @EntityGraph(attributePaths = "exercise")
  List<Attempt> findTop5ByUserOrderByAttemptedAtDesc(User user);
}
