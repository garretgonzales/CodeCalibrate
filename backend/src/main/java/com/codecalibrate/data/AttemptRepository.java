package com.codecalibrate.data;

import com.codecalibrate.models.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {}