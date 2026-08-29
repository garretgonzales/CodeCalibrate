package com.codecalibrate.data;

import com.codecalibrate.models.Skill;
import com.codecalibrate.models.User;
import com.codecalibrate.models.UserMastery;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMasteryRepository extends JpaRepository<UserMastery, Integer> {

  Optional<UserMastery> findByUserAndSkill(User user, Skill skill);
}